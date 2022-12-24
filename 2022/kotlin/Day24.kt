class Day24(input: List<String>) {
    enum class Direction(val offset: Position) {
        Right(Position(0, 1)),
        Down(Position(1, 0)),
        Left(Position(0, -1)),
        Up(Position(-1, 0))
    }

    private val map = input.map { it.toCharArray().toMutableList() }
    private val startPosition = Position(0, map.first().indexOf(EMPTY))
    private val exitPosition = Position(map.lastIndex, map[0].lastIndex - 1)

    // Blizzard simulation cache
    private val blizzardCache = mutableMapOf(
        0 to map.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, col ->
                if (BLIZZARD_DIRECTION.keys.contains(col)) {
                    Position(rowIndex, colIndex) to BLIZZARD_DIRECTION[col]!!
                } else {
                    null
                }
            }
        }
    )

    private fun simulateBlizzard(blizzards: List<Pair<Position, Direction>>): List<Pair<Position, Direction>> {
        return blizzards.map { (pos, dir) ->
            val candidatePosition = pos + dir.offset

            when {
                map.isValid(candidatePosition) && map.get(candidatePosition) != WALL -> candidatePosition
                candidatePosition.col <= 0 -> Position(pos.row, map[0].lastIndex - 1)
                candidatePosition.col >= map[0].lastIndex -> Position(pos.row, 1)
                candidatePosition.row <= 0 -> Position(if (pos.col == map[0].lastIndex - 1) map.lastIndex else map.lastIndex - 1, pos.col)
                candidatePosition.row >= map.lastIndex -> Position(if (pos.col == 1) 0 else 1, pos.col)
                else -> throw IllegalStateException("Invalid candidatePosition: $candidatePosition for pos: $pos - dir: $dir")
            } to dir
        }
    }

    fun solve1(): Int {
        return bfs(startPosition, exitPosition)
    }

    fun solve2(): Int {
        val firstTripMinutes = bfs(startPosition, exitPosition)
        val secondTripMinutes = bfs(exitPosition, startPosition, firstTripMinutes)
        return bfs(startPosition, exitPosition, secondTripMinutes)
    }

    private fun bfs(startPosition: Position, exitPosition: Position, startMinute: Int = 1): Int {
        data class QueueItem(val elf: Position, val minuteCount: Int)

        val queue = ArrayDeque(listOf(QueueItem(startPosition, startMinute)))
        var minMinutes = Int.MAX_VALUE
        val visited = mutableSetOf<Pair<Position, Int>>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val blizzards = blizzardCache.getOrPut(current.minuteCount) {
                simulateBlizzard(blizzardCache[current.minuteCount - 1]!!)
            }.mapTo(mutableSetOf()) { it.first }

            if (current.elf == exitPosition) {
                minMinutes = minOf(minMinutes, current.minuteCount - 1)
                continue
            }

            // We already found a shorter path.
            if (current.minuteCount >= minMinutes) {
                continue
            }

            sequenceOf(
                current.elf + Direction.Down.offset,
                current.elf + Direction.Right.offset,
                current.elf + Direction.Left.offset,
                current.elf + Direction.Up.offset,
                current.elf // Just wait
            ).filter { map.isValid(it) && map.get(it) != WALL && !blizzards.contains(it) }
                .filter { visited.add(it to current.minuteCount) }
                .forEach { newPosition ->
                    queue.addLast(QueueItem(newPosition, current.minuteCount + 1))
                }
        }

        return minMinutes
    }

    companion object {
        private const val WALL = '#'
        private const val EMPTY = '.'
        private val BLIZZARD_DIRECTION = mapOf(
            '>' to Direction.Right,
            'v' to Direction.Down,
            '<' to Direction.Left,
            '^' to Direction.Up
        )
    }
}

fun main() {
    val testSolution = Day24(readInput("day24-test.txt"))

    testSolution.solve1().also {
        check(it == 18) { "Invalid: $it" }
    }

    testSolution.solve2().also {
        check(it == 54) { "Invalid: $it" }
    }

    val solution = Day24(readInput("day24.txt"))
    println(solution.solve1())
    println(solution.solve2())
}
