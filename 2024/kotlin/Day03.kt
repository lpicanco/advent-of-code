package aoc2024

import java.io.File

class Day03(
    private val input: String,
) {
    fun solve1(): Int {
        return execute(input)
    }

    fun solve2(): Int {
        val filterRegex = Regex("""don't\(\)[\s\S]*?do\(\)""")
        return execute(input.replace(filterRegex, ""))
    }

    private fun execute(instructions: String): Int {
        val regex = Regex("""mul\(\d{1,3},\d{1,3}\)""")
        return regex.findAll(instructions).sumOf {
            val (a, b) = it.value.drop(4).dropLast(1).split(",").map { it.toInt() }
            a * b
        }
    }
}

fun main() {
    val testInput1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    val testInput2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    Day03(testInput1).solve1().also {
        check(it == 161) { "Invalid: $it" }
        println(it)
    }
    Day03(File("2024/inputs/day03.txt").readText()).solve1().also {
        println(it)
    }
    Day03(testInput2).solve2().also {
        check(it == 48) { "Invalid: $it" }
        println(it)
    }
    Day03(File("2024/inputs/day03.txt").readText()).solve2().also {
        println(it)
    }
}
