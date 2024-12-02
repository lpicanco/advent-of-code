package aoc2024

import java.io.File

fun readInput(filename: String): List<String> {
    return File("2024/inputs", filename).readLines()
}

fun String.toIntList(separator: String = " "): List<Int> {
    return this.split(separator).map { it.toInt() }
}

fun List<String>.toIntList(separator: String = " "): Sequence<List<Int>> {
    return this.asSequence().map { it.toIntList(separator) }
}