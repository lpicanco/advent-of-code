package aoc2023

import java.io.File

fun readInput(filename: String): List<String> {
    return File("2023/inputs", filename).readLines()
}
