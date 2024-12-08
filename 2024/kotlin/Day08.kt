package aoc2024

class Day08(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)
    private val antennaFrequencies: Map<Char, List<Position2D>> = grid.all { it != '.' }.groupBy { grid.get(it) }

    fun solve1(): Int {
        val total = mutableSetOf<Position2D>()

        allAntennas { antenna, diff ->
            val pos = antenna.plus(diff)
            if (grid.isValid(pos)) {
                total.add(pos)
            }
        }

        return total.size
    }

    fun solve2(): Int {
        val total = mutableSetOf<Position2D>()

        allAntennas { antenna, diff ->
            var pos = antenna.plus(diff)
            total.add(antenna)

            while (grid.isValid(pos)) {
                total.add(pos)
                pos = pos.plus(diff)
            }
        }

        return total.size
    }

    private fun allAntennas(action: (Position2D, Position2D) -> Unit) {
        antennaFrequencies.entries.forEach { (_, positions) ->
            for (antenna in positions) {
                for (antenna2 in positions) {
                    if (antenna == antenna2) {
                        continue
                    }

                    val diff = antenna2.minus(antenna)
                    action(antenna2, diff)
                }
            }
        }
    }
}

fun main() {
    Day08(readInput("day08-test.txt")).solve1().also {
        check(it == 14) { "Invalid: $it" }
        println(it)
    }
    Day08(readInput("day08.txt")).solve1().also {
        println(it)
    }

    Day08(readInput("day08-test.txt")).solve2().also {
        check(it == 34) { "Invalid: $it" }
        println(it)
    }
    Day08(readInput("day08.txt")).solve2().also {
        println(it)
    }
}
