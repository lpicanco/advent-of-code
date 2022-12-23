private typealias Elf = Position

class Day23(private val input: List<String>) {
    private fun parseInput(): List<MutableList<Char>> {
        val list = List(300) { MutableList(300) { EMPTY } }

        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                list[row + 100][col + 100] = char
            }
        }

        return list
    }

    enum class Direction(val offset: Position, val diagonalsToCheck: Pair<Position, Position>) {
        N(Position(-1, 0), Position(-1, -1) to Position(-1, 1)),
        S(Position(1, 0), Position(1, -1) to Position(1, 1)),
        W(Position(0, -1), Position(-1, -1) to Position(1, -1)),
        L(Position(0, 1), Position(-1, 1) to Position(1, 1))
    }

    private fun executeRound(map: List<MutableList<Char>>, directions: ArrayDeque<Direction>): Boolean {
        var moved = false
        val elves: Map<Elf, Position?> = map.flatMapIndexed { row, cols ->
            cols.mapIndexedNotNull { col, char -> if (char == ELF) Position(row, col) else null }
        }.associateWith { elf ->
            val iterator = directions.iterator()
            var move: Position? = null
            var isAlone = true
            while (iterator.hasNext()) {
                val dir = iterator.next()

                val candidateMove = Position(elf.row + dir.offset.row, elf.col + dir.offset.col)

                if ((dir.diagonalsToCheck.toList() + dir.offset).any { pos ->
                    val position = Position(elf.row + pos.row, elf.col + pos.col)
                    map.isValid(position) && map.get(position) == ELF
                }
                ) {
                    isAlone = false
                    continue
                }

                if (move == null && map.isValid(candidateMove)) {
                    move = candidateMove
                }
            }
            if (isAlone) null else move
        }

        elves.entries.groupBy { it.value }.filter { it.value.size == 1 }.values.forEach { list ->
            moved = true
            map.set(list.first().key, EMPTY)
            map.set(list.first().value!!, ELF)
        }

        directions.addLast(directions.removeFirst())
        return moved
    }

    fun solve1(): Int {
        val map = parseInput()
        val directions = ArrayDeque(Direction.values().toList())

        repeat(10) {
            executeRound(map, directions)
        }

        val minRow = map.indexOfFirst { it.contains(ELF) }
        val minCol = map.map { it.indexOfFirst { it == ELF } }.filter { it != -1 }.minOf { it }
        val maxRow = map.indexOfLast { it.contains(ELF) }
        val maxCol = map.maxOf { it.indexOfLast { it == ELF } }

        var count = 0
        for (row in minRow..maxRow) {
            for (col in minCol..maxCol) {
                if (map.get(Position(row, col)) == EMPTY) {
                    count++
                }
            }
        }

        return count
    }

    fun solve2(): Int {
        val map = parseInput()
        val directions = ArrayDeque(Direction.values().toList())

        var rounds = 1
        while (executeRound(map, directions)) {
            rounds++
        }
        return rounds
    }

    companion object {
        private const val ELF = '#'
        private const val EMPTY = '.'
    }
}

fun main() {
    val testSolution = Day23(readInput("day23-test.txt"))
    testSolution.solve1().also {
        check(it == 110) { "Invalid: $it" }
    }

    testSolution.solve2().also {
        check(it == 20) { "Invalid: $it" }
    }

    val solution = Day23(readInput("day23.txt"))

    println(solution.solve1())
    println(solution.solve2())
}
