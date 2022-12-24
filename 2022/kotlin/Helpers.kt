import java.io.File
import kotlin.math.abs

fun readInput(filename: String): List<String> {
    return File("2022/inputs", filename).readLines()
}

fun <T> Iterable<T>.sumOfChar(selector: (T) -> Char?): String {
    var sum = ""
    for (element in this) {
        sum += selector(element) ?: ""
    }
    return sum
}

fun <T> List<MutableList<T>>.set(position: Position, value: T) {
    this[position.row][position.col] = value
}

fun <T> List<List<T>>.isValid(position: Position): Boolean {
    return position.row in this.indices && position.col in this[position.row].indices
}

fun <T> List<List<T>>.get(position: Position): T {
    return this[position.row][position.col]
}

data class Position(var row: Int, var col: Int) {
    fun manhattanDistance(other: Position): Int {
        return abs(row - other.row) + abs(col - other.col)
    }

    operator fun plus(other: Position): Position = Position(row + other.row, col + other.col)
}

data class Vector3(val x: Int, val y: Int, val z: Int)
