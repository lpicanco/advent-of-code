package aoc2023

import java.io.File

fun readInput(filename: String): List<String> {
    return File("2023/inputs", filename).readLines()
}

data class Grid2D(
    val rows: List<List<Char>>,
) {
    val lastRowIndex: Int by lazy { rows.lastIndex }
    val lastColIndex: Int by lazy { rows.first().lastIndex }

    fun get(position: Position2D): Char {
        return rows[position.row][position.col]
    }

    fun isValid(position: Position2D): Boolean {
        return position.row in rows.indices && position.col in rows[position.row].indices
    }

    fun firstAdjacentOrNull(
        position: Position2D,
        predicate: (Char) -> Boolean,
    ): Position2D? {
        return adjacent(position).firstOrNull { predicate(get(it)) }
    }

    private fun adjacent(position: Position2D): List<Position2D> {
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

    companion object {
        fun parse(lines: List<String>): Grid2D {
            return Grid2D(lines.map { line -> line.map { it } })
        }
    }
}

data class Position2D(val row: Int, val col: Int) {
    fun plus(
        row: Int,
        col: Int,
    ): Position2D = Position2D(this.row + row, this.col + col)

    fun plus(other: Position2D): Position2D = plus(other.row, other.col)
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
