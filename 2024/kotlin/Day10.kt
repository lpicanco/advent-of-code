package aoc2024

class Day10(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)
    private val visited = mutableSetOf<Pair<Position2D, Position2D>>()

    fun solve1(): Int {
        return grid.all { it == '0' }
            .sumOf { findNine(it, it, false) }
    }

    fun solve2(): Int {
        return grid.all { it == '0' }
            .sumOf { findNine(it, it, true) }
    }

    private fun findNine(
        start: Position2D,
        current: Position2D,
        findAll: Boolean,
    ): Int {
        val value = grid.get(current).digitToInt()
        if (value == 9) {
            return if (findAll || visited.add(start to current)) 1 else 0
        }

        return current.orthogonalDirections
            .map { current.plus(it) }
            .filter {
                grid.isMatch(it, value.plus(1).digitToChar())
            }.sumOf {
                findNine(start, it, findAll)
            }
    }
}

fun main() {
    Day10(readInput("day10-test.txt")).solve1().also {
        check(it == 36) { "Invalid: $it" }
        println(it)
    }
    Day10(readInput("day10.txt")).solve1().also {
        println(it)
    }

    Day10(readInput("day10-test.txt")).solve2().also {
        check(it == 81) { "Invalid: $it" }
        println(it)
    }
    Day10(readInput("day10.txt")).solve2().also {
        println(it)
    }
}
