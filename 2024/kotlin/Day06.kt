package aoc2024

class Day06(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)
    private val startPosition = grid.first { it == '^' }

    fun solve1(): Int {
        return countStepsToExit()!!
    }

    fun solve2(): Int {
        var obstructions = 0
        for (y in grid.rowIndices) {
            for (x in grid.colIndices) {
                val obstruction = Position2D(x, y)
                if (grid.get(obstruction) == '.' && countStepsToExit(obstruction) == null) {
                    obstructions++
                }
            }
        }
        return obstructions
    }

    private fun countStepsToExit(obstruction: Position2D? = null): Int? {
        var position = startPosition
        var direction = Direction.Up
        val seen = mutableSetOf(position)
        val seenWithDir = mutableSetOf<Pair<Position2D, Direction>>()

        while (grid.isValid(position)) {
            if (!seenWithDir.add(position to direction)) {
                return null
            }

            seen.add(position)
            val candidatePosition = position.plus(direction)
            if (grid.isValid(candidatePosition) && (
                    grid.get(candidatePosition) == '#' || candidatePosition == (
                        obstruction
                            ?: false
                    )
                )
            ) {
                direction = direction.turnRight
                continue
            }

            position = candidatePosition
        }
        return seen.size
    }
}

fun main() {
    Day06(readInput("day06-test.txt")).solve1().also {
        check(it == 41) { "Invalid: $it" }
        println(it)
    }
    Day06(readInput("day06.txt")).solve1().also {
        println(it)
    }

    Day06(readInput("day06-test.txt")).solve2().also {
        check(it == 6) { "Invalid: $it" }
        println(it)
    }
    Day06(readInput("day06.txt")).solve2().also {
        println(it)
    }
}
