const val start = 'S'
const val end = 'E'

fun List<List<Char>>.getTranslated(position: Position): Char {
    return when (val value = get(position)) {
        start -> 'a'
        end -> 'z'
        else -> value
    }
}

private fun part1(input: List<String>): Int {
    val grid = readGrid(input)
    val position = findChar(start, grid)
    return bfs(position, end, grid) { current, next ->
        (next <= current + 1 && next.isLowerCase())
    }
}

private fun part2(input: List<String>): Int {
    val grid = readGrid(input)
    val position = findChar(end, grid)
    return bfs(position, 'a', grid) { current, next -> (current <= next + 1) }
}

fun bfs(startPosition: Position, charToFind: Char, grid: List<List<Char>>, filter: (Char, Char) -> Boolean): Int {
    val queue = ArrayDeque(listOf(startPosition to 0))
    val visited = mutableSetOf(startPosition)

    var minStep = Int.MAX_VALUE

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val (row, col) = current.first

        if (grid.get(current.first) == charToFind) {
            minStep = minOf(minStep, current.second)
        }

        sequenceOf(
            Position(row - 1, col),
            Position(row + 1, col),
            Position(row, col - 1),
            Position(row, col + 1),
        ).filter { it.row in grid.indices && it.col in grid[0].indices }
            .filter {
                filter(grid.getTranslated(current.first), grid.getTranslated(it))
            }
            .filter { visited.add(it) }
            .forEach {
                queue.addLast(it to current.second + 1)
            }
    }
    return minStep
}

private fun findChar(char: Char, grid: List<List<Char>>): Position {
    for (row in grid.indices) {
        for (col in grid[0].indices) {
            if (grid[row][col] == char) {
                return Position(row, col)
            }
        }
    }
    throw IllegalArgumentException("Char $char not found")
}

fun readGrid(input: List<String>): List<List<Char>> {
    return input.map { it.toCharArray().toList() }
}

fun main() {
    val inputTest = readInput("day12-test.txt")
    check(part1(inputTest) == 31) { "Invalid: ${part1(inputTest)}" }
    check(part2(inputTest) == 29) { "Invalid: ${part2(inputTest)}" }

    val input = readInput("day12.txt")

    println(part1(input))
    println(part2(input))
}
