package aoc2024

import kotlin.math.abs

class Day01(
    val lines: List<String>,
) {
    private val listA: List<Int>
    private val listB: List<Int>

    init {
        val lists = lines.map { it.split("   ") }
        listA = lists.map { it[0].toInt() }
        listB = lists.map { it[1].toInt() }
    }

    fun solve1(): Int {
        return listA.sorted().zip(listB.sorted()) { a, b -> abs(a - b) }.sum()
    }

    fun solve2(): Int {
        val listBCount = listB.groupingBy { it }.eachCount()

        return listA.sumOf {
            it * listBCount.getOrDefault(it, 0)
        }
    }
}

fun main() {
    Day01(readInput("day01-test.txt")).solve1().also {
        check(it == 11) { "Invalid: $it" }
        println(it)
    }

    Day01(readInput("day01-test.txt")).solve2().also {
        check(it == 31) { "Invalid: $it" }
        println(it)
    }
}
