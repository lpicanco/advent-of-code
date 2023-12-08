package aoc2023

class Day07(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        return lines.map(::parse)
            .sortedWith(
                compareBy(Hand::score).thenBy { it },
            ).mapIndexed { index: Int, hand: Hand ->
                hand.bid * (index + 1)
            }.sum()
    }

    fun solve2(): Int {
        return lines.map { parse(it, applyJokerRule = true) }
            .sortedWith(
                compareBy(Hand::score).thenBy { it },
            ).mapIndexed { index: Int, hand: Hand ->
                hand.bid * (index + 1)
            }.sum()
    }

    private fun parse(
        line: String,
        applyJokerRule: Boolean = false,
    ): Hand {
        val (cardStr, bidStr) = line.split(" ")
        val cards = cardStr.map { calculateStrength(it, applyJokerRule) }

        if (applyJokerRule) {
            val score = calculateScoreUsingJoker(cards)
            return Hand(cards, score, bidStr.toInt())
        }

        val score = calculateScore(cards)
        return Hand(cards, score, bidStr.toInt())
    }

    private fun calculateStrength(
        card: Char,
        applyJokerRule: Boolean,
    ): Int {
        val jStrength = if (applyJokerRule) JOKER_VALUE else 11
        return when (card) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> jStrength
            'T' -> 10
            else -> card.toString().toInt()
        }
    }

    private fun calculateScoreUsingJoker(cards: List<Int>): Int {
        val jokerIndices = cards.mapIndexedNotNull { index, i -> if (i == JOKER_VALUE) index else null }
        val allCombinations = mutableListOf<List<Int>>()
        val max = 14
        val min = 1
        val range = (min..max).filter { it != 11 }.toList()
        range.forEach { i ->
            val newList = cards.toMutableList()
            jokerIndices.forEach { jokerIndex ->
                newList[jokerIndex] = i
            }
            allCombinations.add(newList)
        }

        val scores = allCombinations.map { calculateScore(it) }
        val maxScore = scores.maxOrNull() ?: 0

        return maxScore
    }

    private fun calculateScore(cards: List<Int>): Int {
        val counts = cards.groupingBy { it }.eachCount()
        return when {
            counts.values.any { it == 5 } -> 7
            counts.values.any { it == 4 } -> 6
            counts.values.any { it == 3 } && counts.size == 2 -> 5
            counts.values.any { it == 3 } -> 4
            counts.values.count { it == 2 } == 2 -> 3
            counts.values.any { it == 2 } -> 2
            else -> 1
        }
    }

    data class Hand(val cards: List<Int>, val score: Int, val bid: Int) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            return compareValuesBy(this, other, { it.cards[0] }, { it.cards[1] }, { it.cards[2] }, { it.cards[3] }, { it.cards[4] })
        }
    }

    companion object {
        const val JOKER_VALUE = 1
    }
}

fun main() {
    Day07(readInput("day07-test.txt")).run {
        solve1().also {
            check(it == 6440) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 5905) { "Invalid: $it" }
            println(it)
        }
    }

    Day07(readInput("day07.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
