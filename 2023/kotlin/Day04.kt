package aoc2023

import kotlin.math.pow

class Day04(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        return lines.map(Card.Companion::parse)
            .sumOf { it.calculatePoints() }
    }

    fun solve2(): Int {
        val cards = lines.map { Card.parse(it) }
        var cardCount = cards.size
        val queue = ArrayDeque(cards)
        val cache = mutableMapOf<Card, Pair<Int, List<Card>>>()

        while (queue.isNotEmpty()) {
            val card = queue.removeFirst()
            val (matches, newCards) =
                cache.getOrPut(card) {
                    val matches = card.calculateMatches()
                    val nextCardId = cards.indexOf(card) + 1

                    if (nextCardId in cards.indices) {
                        matches to cards.subList(nextCardId, minOf(nextCardId + matches, cards.lastIndex))
                    } else {
                        matches to emptyList()
                    }
                }
            cardCount += matches
            queue.addAll(newCards)
        }

        return cardCount
    }

    data class Card(val winNumbers: List<Int>, val myNumbers: List<Int>) {
        fun calculatePoints(): Int {
            val matchers = calculateMatches()
            if (matchers == 0) {
                return 0
            }

            return 2.0.pow(matchers - 1).toInt()
        }

        fun calculateMatches() = winNumbers.intersect(myNumbers).size

        companion object {
            fun parse(line: String): Card {
                val numbers = line.substringAfter(": ").split("|")
                val winNumbers = numbers.first().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
                val myNumbers = numbers.last().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
                return Card(winNumbers, myNumbers)
            }
        }
    }
}

fun main() {
    Day04(readInput("day04-test.txt")).run {
        solve1().also {
            check(it == 13) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 30) { "Invalid: $it" }
            println(it)
        }
    }

    Day04(readInput("day04.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
