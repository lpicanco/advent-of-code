package aoc2023

class Day01(
    private val lines: List<String>
) {
    fun solve1(): Int {
        val sum = lines.sumOf { line ->
            var i = 0
            var j = line.lastIndex
            var left: Char? = null
            var right: Char? = null

            while (left == null || right == null) {
                if (line[i].isDigit()) {
                    left = line[i]
                } else {
                    i++
                }

                if (line[j].isDigit()) {
                    right = line[j]
                } else {
                    j--
                }
            }

            (left.toString() + right.toString()).toInt()
        }
        return sum
    }

    fun solve2(): Int {
        return lines.sumOf { line ->
            var left: String? = null
            var right: String? = null

            var i = 0
            while(left == null) {
                left = getNumberOrNull(line, i)
                i++
            }

            var j = line.lastIndex
            while(right == null) {
                right = getNumberOrNull(line, j)
                j--
            }
            (left + right).toInt()
        }
    }

    private fun getNumberOrNull(line: String, x: Int): String? {
        if (line[x].isDigit()) {
            return line[x].toString()
        }

        SPELLED_NUMBERS.forEachIndexed { index, spelledNumber ->
            if (x + spelledNumber.length <= line.length) {
                if (line.substring(x, x + spelledNumber.length) == spelledNumber) {
                    return index.toString()
                }
            }
        }

        return null
    }

    companion object {
        val SPELLED_NUMBERS: List<String> = listOf(
            "zero",
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten"
        )
    }
}

fun main() {
    Day01(readInput("day01-01-test.txt")).solve1().also {
        check(it == 142) { "Invalid: $it" }
        println(it)
    }

    Day01(readInput("day01-02-test.txt")).solve2().also {
        check(it == 281) { "Invalid: $it" }
        println(it)
    }

    Day01(readInput("day01.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}