package aoc2023

class Day09(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        return lines.map(::parse)
            .sumOf { calculateNextNumber(it) }
    }

    fun solve2(): Int {
        return lines.map(::parse)
            .sumOf { calculatePreviousNumber(it) }
    }

    private fun parse(line: String): List<Int> = line.split(" ").map { it.trim().toInt() }

    private fun calculateNextNumber(numbers: List<Int>): Int {
        if (numbers.all { it == 0 }) {
            return 0
        }

        val numbersDiff =
            numbers.drop(1).mapIndexed { index: Int, number: Int ->
                number - numbers[index]
            }
        return numbers.last() + calculateNextNumber(numbersDiff)
    }

    private fun calculatePreviousNumber(numbers: List<Int>): Int {
        if (numbers.all { it == 0 }) {
            return 0
        }

        val numbersDiff =
            numbers.drop(1).mapIndexed { index: Int, number: Int ->
                number - numbers[index]
            }
        return numbers.first() - calculatePreviousNumber(numbersDiff)
    }
}

fun main() {
    Day09(readInput("day09-test.txt")).run {
        solve1().also {
            check(it == 114) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 2) { "Invalid: $it" }
            println(it)
        }
    }
    Day09(readInput("day09.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
