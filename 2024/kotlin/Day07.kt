package aoc2024

class Day07(
    lines: List<String>,
) {
    private val numbers: Set<Pair<Long, List<Int>>> =
        lines.map { it.split(":") }.map {
            it[0].toLong() to it[1].split(" ").mapNotNull { it.toIntOrNull() }
        }.toSet()

    fun solve1(): Long {
        return numbers.filter { evaluate(it.first, it.second, 2) }
            .sumOf { it.first }
    }

    fun solve2(): Long {
        return numbers.filter { evaluate(it.first, it.second, 3) }
            .sumOf { it.first }
    }

    private fun evaluate(
        expected: Long,
        numbers: List<Int>,
        combinations: Int,
        current: Long = 0,
    ): Boolean {
        if (numbers.isEmpty()) {
            return current == expected
        }

        if (current > expected) {
            return false
        }

        val first = numbers.first()
        val rest = numbers.drop(1)

        return if (combinations == 2) {
            evaluate(expected, rest, combinations, current + first) ||
                evaluate(expected, rest, combinations, current * first)
        } else {
            evaluate(expected, rest, combinations, current + first) ||
                evaluate(expected, rest, combinations, current * first) ||
                evaluate(expected, rest, combinations, "$current$first".toLong())
        }
    }
}

fun main() {
    Day07(readInput("day07-test.txt")).solve1().also {
        check(it == 3749L) { "Invalid: $it" }
        println(it)
    }
    Day07(readInput("day07.txt")).solve1().also {
        println(it)
    }

    Day07(readInput("day07-test.txt")).solve2().also {
        check(it == 11387L) { "Invalid: $it" }
        println(it)
    }
    Day07(readInput("day07.txt")).solve2().also {
        println(it)
    }
}
