class Day20(val input: List<String>) {
    private fun solve(numbers: List<Long>, times: Int): Long {
        val positions = numbers.withIndex().toMutableList()
        for (i in 0 until numbers.size * times) {
            val index = i % numbers.size
            val numberPosition = positions.indexOfFirst { it.index == index }
            val number = positions[numberPosition]

            positions.removeAt(numberPosition)
            positions.add((numberPosition + number.value).mod(positions.size), number)
        }

        val indexOfZero = positions.indexOfFirst { it.value == 0L }
        return setOf(1000, 2000, 3000).sumOf { positions[(indexOfZero + it).mod(positions.size)].value }
    }

    fun solve1(): Long {
        return solve(input.map { it.toLong() }, 1)
    }

    fun solve2(): Long {
        return solve(input.map { it.toLong() * 811589153L }, 10)
    }
}

fun main() {
    val testSolution = Day20(readInput("day20-test.txt"))

    testSolution.solve1().also {
        check(it == 3L) { "Invalid: $it" }
    }

    testSolution.solve2().also {
        check(it == 1623178306L) { "Invalid: $it" }
    }

    val solution = Day20(readInput("day20.txt"))
    println(solution.solve1())
    println(solution.solve2())
}
