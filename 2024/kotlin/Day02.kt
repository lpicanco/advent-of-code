package aoc2024

import kotlin.math.abs

class Day02(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        return lines.toIntList().count { it.isSafe() }
    }

    fun solve2(): Int {
        return lines.toIntList().count { report ->
            report.isSafe() || report.isSafeWithProblemDampener()
        }
    }

    private fun List<Int>.isSafe(): Boolean {
        val inc = this[0] < this[1]
        return !windowed(2).any { (a, b) ->
            (inc && a >= b) || abs(a - b) !in 1..3 || (!inc && a <= b)
        }
    }

    private fun List<Int>.isSafeWithProblemDampener() =
        this.indices.map { this.subList(0, it) + this.subList(it + 1, this.size) }.any { it.isSafe() }
}

fun main() {
    Day02(readInput("day02-test.txt")).solve1().also {
        check(it == 2) { "Invalid: $it" }
        println(it)
    }
    Day02(readInput("day02-test.txt")).solve2().also {
        check(it == 4) { "Invalid: $it" }
        println(it)
    }
}
