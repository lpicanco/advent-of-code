package aoc2024

import java.io.File

fun readInput(filename: String): List<String> {
    return File("2024/inputs", filename).readLines()
}