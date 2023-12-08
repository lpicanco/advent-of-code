package aoc2023

class Day06(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        val durations = parseWithSplitNumbers(lines[0])
        val maxDistances = parseWithSplitNumbers(lines[1])

        return durations.mapIndexed { index, duration ->
            val maxDistance = maxDistances[index]
            countBestDistances(duration.toInt(), maxDistance)
        }.reduce { acc, i -> acc * i }
    }

    fun solve2(): Int {
        val duration = parseWithUniqueNumber(lines[0])
        val maxDistance = parseWithUniqueNumber(lines[1])

        return countBestDistances(duration.toInt(), maxDistance)
    }

    private fun countBestDistances(duration: Int, maxDistance: Long): Int {
        var count = 0
        repeat(duration) {
            val traveledDistance = (it.toLong()) * (duration - it)
            if (traveledDistance > maxDistance) {
                count++
            }
        }
        return count
    }

    private fun parseWithSplitNumbers(line: String): List<Long> {
        return line.substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
    }
    private fun parseWithUniqueNumber(line: String): Long {
        return line.substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.trim() }.joinToString("").toLong()
    }
}

fun main() {
    Day06(readInput("day06-test.txt")).run {
        solve1().also {
            check(it == 288) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 71503) { "Invalid: $it" }
            println(it)
        }
    }

    Day06(readInput("day06.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
