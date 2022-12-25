class Day25(val input: List<String>) {
    private fun toDecimal(expression: String): Long {
        return expression.fold(0) { acc, char ->
            (acc * 5) + SNAFU_TO_DECIMAL_TABLE[char]!!
        }
    }

    private fun toSnafu(value: Long): String {
        if (value == 0L) {
            return ""
        }

        val quotient = value / 5
        val remainder = value % 5

        return if (remainder <= 2) {
            toSnafu(quotient) + DECIMAL_TO_SNAFU_TABLE[remainder]
        } else {
            toSnafu(quotient + 1) + DECIMAL_TO_SNAFU_TABLE[remainder - 5]
        }
    }

    fun solve1() = toSnafu(input.sumOf(::toDecimal))

    companion object {
        private val SNAFU_TO_DECIMAL_TABLE: Map<Char, Long> = mapOf(
            '0' to 0,
            '1' to 1,
            '2' to 2,
            '-' to -1,
            '=' to -2,
        )

        private val DECIMAL_TO_SNAFU_TABLE = SNAFU_TO_DECIMAL_TABLE.entries.associate { it.value to it.key }
    }
}

fun main() {
    val testSolution = Day25(readInput("day25-test.txt"))

    testSolution.solve1().also {
        check(it == "2=-1=0") { "Invalid: $it" }
    }

    val solution = Day25(readInput("day25.txt"))
    println(solution.solve1())
}
