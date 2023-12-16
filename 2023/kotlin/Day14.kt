package aoc2023

class Day14(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        val grid = Grid2D.parse(lines)
        roll(grid, Direction.Up)
        return calculateLoad(grid)
    }

    fun solve2(): Int {
        val grid = Grid2D.parse(lines)
        val seen = mutableMapOf<List<Position2D>, Int>()
        val remainingCycles: Int
        val totalCycles = 1_000_000_000
        var currentCycle = 0
        while (true) {
            val roundedRocks = grid.all { it == 'O' }

            if (roundedRocks in seen) {
                remainingCycles = (totalCycles - currentCycle) % (currentCycle - seen.getValue(roundedRocks))
                break
            }

            seen[roundedRocks] = currentCycle
            runCycle(grid)
            currentCycle++
        }

        for (i in 0 until remainingCycles) {
            runCycle(grid)
        }

        return calculateLoad(grid)
    }

    private fun runCycle(grid: Grid2D) {
        roll(grid, Direction.Up)
        roll(grid, Direction.Left)
        roll(grid, Direction.Down)
        roll(grid, Direction.Right)
    }

    private fun roll(
        grid: Grid2D,
        direction: Direction,
    ) {
        var moved = true
        while (moved) {
            moved = false
            for (rock in grid.all { it == 'O' }) {
                val candidatePosition = rock.plus(direction.offset)
                if (grid.isValid(candidatePosition) && grid.get(candidatePosition) == '.') {
                    grid.set(rock, '.')
                    grid.set(candidatePosition, 'O')
                    moved = true
                }
            }
        }
    }

    private fun calculateLoad(grid: Grid2D): Int {
        val roundedRocks = grid.all { it == 'O' }
        return roundedRocks.sumOf {
            grid.lastColIndex + 1 - it.y
        }
    }
}

fun main() {
    Day14(readInput("day14-test.txt")).run {
        solve1().also {
            check(it == 136) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 64) { "Invalid: $it" }
            println(it)
        }
    }

    Day14(readInput("day14.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
