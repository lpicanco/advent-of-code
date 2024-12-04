package aoc2024

import java.io.File
import kotlin.math.abs

fun readInput(filename: String): List<String> {
    return File("2024/inputs", filename).readLines()
}

fun String.toIntList(separator: String = " "): List<Int> {
    return this.split(separator).map { it.toInt() }
}

fun List<String>.toIntList(separator: String = " "): Sequence<List<Int>> {
    return this.asSequence().map { it.toIntList(separator) }
}

enum class Direction(val offset: Position2D, val char: Char) {
    Up(Position2D(0, -1), '^'),
    UpRight(Position2D(1, -1), '/'),
    Right(Position2D(1, 0), '>'),
    DownRight(Position2D(1, 1), '\\'),
    Down(Position2D(0, 1), 'v'),
    DownLeft(Position2D(-1, 1), '/'),
    Left(Position2D(-1, 0), '<'),
    UpLeft(Position2D(-1, -1), '\\'),
}

data class Position2D(val x: Int, val y: Int) {
    val orthogonalDirections: List<Direction> by lazy {
        listOf(
            Direction.Up,
            Direction.Left,
            Direction.Right,
            Direction.Down,
        )
    }

    val adjacentDirections: List<Direction> by lazy {
        Direction.entries
    }

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

    fun plus(other: Direction): Position2D = plus(other.offset)

    fun minus(other: Position2D): Position2D = minus(other.y, other.x)
}

data class Grid2D(
    val rows: List<MutableList<Char>>,
) {
    val rowIndices: IntRange by lazy { rows.indices }
    val colIndices: IntRange by lazy { rows.first().indices }

    fun get(position: Position2D): Char {
        return rows[position.y][position.x]
    }

    fun isMatch(
        position: Position2D,
        value: Char,
    ): Boolean {
        return isValid(position) && get(position) == value
    }

    fun set(
        position: Position2D,
        value: Char,
    ) {
        rows[position.y][position.x] = value
    }

    fun isValid(position: Position2D): Boolean {
        return position.y in rows.indices && position.x in rows[position.y].indices
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
