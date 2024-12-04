package aoc2024

class Day04(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)

    fun solve1(): Int {
        return grid.rowIndices.sumOf { row ->
            grid.colIndices.sumOf { col ->
                val pos = Position2D(col, row)
                if (grid.get(pos) == 'X') {
                    pos.adjacentDirections.count {
                        findString(pos.plus(it), it, "MAS")
                    }
                } else {
                    0
                }
            }
        }
    }

    fun solve2(): Int {
        return grid.rowIndices.sumOf { row ->
            grid.colIndices.sumOf { col ->
                val pos = Position2D(col, row)
                val result =
                    if (grid.get(pos) == 'A' &&
                        (
                            findString(pos.plus(Direction.UpLeft), Direction.DownRight, "MAS") ||
                                findString(pos.plus(Direction.DownRight), Direction.UpLeft, "MAS")
                        ) &&
                        (
                            findString(pos.plus(Direction.UpRight), Direction.DownLeft, "MAS") ||
                                findString(pos.plus(Direction.DownLeft), Direction.UpRight, "MAS")
                        )
                    ) {
                        1
                    } else {
                        0
                    }
                result
            }
        }
    }

    private fun findString(
        start: Position2D,
        direction: Direction,
        string: String,
    ): Boolean {
        if (!grid.isValid(start)) {
            return false
        }

        if (!grid.isMatch(start, string[0])) {
            return false
        }

        if (string.length == 1) {
            return true
        }

        return findString(start.plus(direction), direction, string.drop(1))
    }
}

fun main() {
    Day04(readInput("day04-test.txt")).solve1().also {
        check(it == 18) { "Invalid: $it" }
        println(it)
    }
    Day04(readInput("day04.txt")).solve1().also {
        println(it)
    }

    Day04(readInput("day04-test.txt")).solve2().also {
        check(it == 9) { "Invalid: $it" }
        println(it)
    }
    Day04(readInput("day04.txt")).solve2().also {
        println(it)
    }
}
