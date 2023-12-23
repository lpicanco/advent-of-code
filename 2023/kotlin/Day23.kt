package aoc2023

class Day23(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)
    private val start = grid.first { it == '.' }
    private val end = Position2D(grid.rows.last().lastIndexOf('.'), grid.lastRowIndex)

    fun solve1(): Int {
        return solve()
    }
    fun solve2(): Int {
        return solve(ignoreSlopes = true)
    }

    private fun solve(ignoreSlopes: Boolean = false): Int {
        val queue = ArrayDeque<Pair<Position2D, Set<Position2D>>>()
        queue.addFirst(start to emptySet())
        var maxWalk = 0

        while (queue.isNotEmpty()) {
            val (current, visited) = queue.removeFirst()

            if (current == end) {
                maxWalk = maxOf(visited.size, visited.size)
                continue
            }

            if (current in visited) {
                continue
            }

            Direction.entries.filter { !ignoreSlopes && it.char == grid.get(current) }
                .ifEmpty { current.orthogonalDirections }
                .map { current.plus(it.offset) }
                .filter { grid.isValid(it) && grid.get(it) != '#' && !visited.contains(it) }
                .forEach {
                    queue.addFirst(it to visited + current)
                }
        }

        return maxWalk
    }
}

fun main() {
    Day23(readInput("day23-test.txt")).run {
        solve1().also {
            check(it == 94) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 154) { "Invalid: $it" }
            println(it)
        }
    }

    Day23(readInput("day23.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
