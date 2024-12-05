package aoc2024

class Day05(
    lines: List<String>,
) {
    private val pageOrder: Map<Int, Set<Int>> =
        lines.takeWhile { it.isNotEmpty() }
            .map { it.split("|") }
            .groupBy { it[0].toInt() }
            .mapValues { it.value.map { it[1].toInt() }.toSet() }

    private val pages: List<List<Int>> =
        lines.dropWhile { it.isNotEmpty() }.drop(1)
            .map { it.split(",").map { it.toInt() } }

    fun solve1(): Int {
        return pages.filter(::isSorted)
            .sumOf { findMiddle(it) }
    }

    fun solve2(): Int {
        return pages.filterNot(::isSorted)
            .map(::sortPage)
            .sumOf { findMiddle(it) }
    }

    private fun isSorted(page: List<Int>): Boolean {
        return sortPage(page) == page
    }

    private fun sortPage(page: List<Int>) =
        page.sortedWith { a, b ->
            if (a in pageOrder && b in pageOrder[a]!!) {
                -1
            } else if (b in pageOrder && a in pageOrder[b]!!) {
                1
            } else {
                0
            }
        }

    private fun findMiddle(it: List<Int>) = it[(it.size / 2)]
}

fun main() {
    Day05(readInput("day05-test.txt")).solve1().also {
        check(it == 143) { "Invalid: $it" }
        println(it)
    }
    Day05(readInput("day05.txt")).solve1().also {
        println(it)
    }

    Day05(readInput("day05-test.txt")).solve2().also {
        check(it == 123) { "Invalid: $it" }
        println(it)
    }
    Day05(readInput("day05.txt")).solve2().also {
        println(it)
    }
}
