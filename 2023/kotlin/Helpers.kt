package aoc2023

import java.io.File
import kotlin.math.abs

fun readInput(filename: String): List<String> {
    return File("2023/inputs", filename).readLines()
}

data class Grid2D(
    val rows: List<MutableList<Char>>,
) {
    val lastRowIndex: Int by lazy { rows.lastIndex }
    val lastColIndex: Int by lazy { rows.first().lastIndex }

    val rowIndices: IntRange by lazy { rows.indices }
    val colIndices: IntRange by lazy { rows.first().indices }

    fun get(position: Position2D): Char {
        return rows[position.y][position.x]
    }

    fun set(
        position: Position2D,
        value: Char,
    ) {
        rows[position.y][position.x] = value
    }

    fun setCopy(
        position: Position2D,
        value: Char,
    ): Grid2D {
        return copy(
            rows =
                rows.mapIndexed { index, list ->
                    if (index == position.y) {
                        list.mapIndexed { colIndex, c ->
                            if (colIndex == position.x) {
                                value
                            } else {
                                c
                            }
                        }
                    } else {
                        list
                    }.toMutableList()
                },
        )
    }

    fun isValid(position: Position2D): Boolean {
        return position.y in rows.indices && position.x in rows[position.y].indices
    }

    fun firstAdjacentOrNull(
        position: Position2D,
        predicate: (Char) -> Boolean,
    ): Position2D? {
        return adjacent(position).firstOrNull { predicate(get(it)) }
    }

    fun firstOrthogonalAdjacentOrNull(
        position: Position2D,
        predicate: (Position2D, Char) -> Boolean,
    ): Position2D? {
        return orthogonalAdjacent(position).firstOrNull { predicate(it, get(it)) }
    }

    fun adjacent(position: Position2D): List<Position2D> {
        return listOf(
            position.plus(-1, -1),
            position.plus(-1, 0),
            position.plus(-1, 1),
            position.plus(0, -1),
            position.plus(0, 1),
            position.plus(1, -1),
            position.plus(1, 0),
            position.plus(1, 1),
        ).filter { isValid(it) }
    }

    fun orthogonalAdjacent(position: Position2D): List<Position2D> {
        return listOf(
            position.plus(-1, 0),
            position.plus(0, -1),
            position.plus(0, 1),
            position.plus(1, 0),
        ).filter { isValid(it) }
    }

    fun columnToString(columnIndex: Int): String {
        return rows.map { it[columnIndex] }.joinToString("")
    }

    fun first(predicate: (Char) -> Boolean): Position2D {
        for (y in rows.indices) {
            for (x in rows[y].indices) {
                if (predicate(rows[y][x])) {
                    return Position2D(x, y)
                }
            }
        }

        throw NoSuchElementException("No element matching predicate was found")
    }

    fun all(predicate: (Char) -> Boolean): List<Position2D> {
        val positions = mutableListOf<Position2D>()
        for (y in rows.indices) {
            for (x in rows[y].indices) {
                if (predicate(rows[y][x])) {
                    positions.add(Position2D(x, y))
                }
            }
        }

        return positions
    }

    fun print() {
        for (y in rows.indices) {
            for (x in rows[y].indices) {
                print(get(Position2D(x, y)))
            }
            println()
        }
        repeat(50) { print("---") }
        println()
    }

    companion object {
        fun parse(lines: List<String>): Grid2D {
            return Grid2D(lines.map { line -> line.map { it }.toMutableList() })
        }
    }
}

data class Position2D(val x: Int, val y: Int) {
    fun plus(
        row: Int,
        col: Int,
    ): Position2D = Position2D(this.x + col, this.y + row)

    fun manhattanDistance(other: Position2D): Int {
        return abs(y - other.y) + abs(x - other.x)
    }

    fun minus(
        row: Int,
        col: Int,
    ): Position2D = Position2D(this.x - col, this.y - row)

    fun plus(other: Position2D): Position2D = plus(other.y, other.x)

    fun minus(other: Position2D): Position2D = minus(other.y, other.x)
}

enum class Direction(val offset: Position2D, val char: Char) {
    Right(Position2D(1, 0), '>'),
    Down(Position2D(0, 1), 'v'),
    Left(Position2D(-1, 0), '<'),
    Up(Position2D(0, -1), '^'),
}

fun lcm(
    a: Long,
    b: Long,
): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun lcm(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = lcm(result, numbers[i])
    }
    return result
}
