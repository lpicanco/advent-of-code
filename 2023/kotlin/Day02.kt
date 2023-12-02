package aoc2023

class Day02(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        return lines.withIndex().sumOf { (index, line) ->
            val maxSet = RGB.parse(line).maxOf()
            if (maxSet.r <= 12 && maxSet.g <= 13 && maxSet.b <= 14) {
                index + 1
            } else {
                0
            }
        }
    }

    fun solve2(): Int {
        return lines.sumOf { line ->
            val maxSet = RGB.parse(line).maxOf()
            maxSet.multiply()
        }
    }

    data class RGB(val r: Int, val g: Int, val b: Int) {
        fun multiply(): Int {
            return r * g * b
        }

        companion object {
            fun parse(line: String): List<RGB> {
                return line.substringAfter(": ").split(";").map { set ->
                    val colors = set.split(",").map { it.trim() }
                    val r = colors.firstOrNull { it.contains("red") }?.substringBefore(" ")?.toInt() ?: 0
                    val g = colors.firstOrNull { it.contains("green") }?.substringBefore(" ")?.toInt() ?: 0
                    val b = colors.firstOrNull { it.contains("blue") }?.substringBefore(" ")?.toInt() ?: 0
                    RGB(r, g, b)
                }
            }
        }
    }

    private fun List<RGB>.maxOf(): RGB {
        val r = maxOf { it.r }
        val g = maxOf { it.g }
        val b = maxOf { it.b }
        return RGB(r, g, b)
    }
}

fun main() {
    Day02(readInput("day02-test.txt")).run {
        solve1().also {
            check(it == 8) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 2286) { "Invalid: $it" }
            println(it)
        }
    }

    Day02(readInput("day02.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
