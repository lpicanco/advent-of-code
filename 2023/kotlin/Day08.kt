package aoc2023

class Day08(
    lines: List<String>,
) {
    private val instructions = lines[0].map(::parseInstruction)
    private val map = lines.drop(2).associate(::parseMap)

    fun solve1(): Long {
        val start = map.getValue("AAA")
        return solve(start) {
            it == "ZZZ"
        }
    }

    fun solve2(): Long {
        val solutions =
            map.keys
                .filter { it[2] == 'A' }
                .map {
                    solve(map.getValue(it)) { key ->
                        key[2] == 'Z'
                    }
                }

        return lcm(solutions)
    }

    private fun solve(
        start: Array<String>,
        predicate: (String) -> Boolean,
    ): Long {
        var steps = 0L
        var current = start
        var instructionIndex = 0

        while (true) {
            steps++
            val position = instructions[instructionIndex].position
            val key = current[position]
            if (predicate(key)) {
                break
            }
            current = map.getValue(key)
            instructionIndex = (instructionIndex + 1) % instructions.size
        }

        return steps
    }

    private fun parseMap(line: String): Pair<String, Array<String>> {
        val key = line.substring(0..2)
        val left = line.substring(7..9)
        val right = line.substring(12..14)
        return key to arrayOf(left, right)
    }

    private fun parseInstruction(instruction: Char) =
        when (instruction) {
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> throw IllegalArgumentException("Invalid instruction: $instruction")
        }

    enum class Direction(val position: Int) {
        LEFT(0),
        RIGHT(1),
    }
}

fun main() {
    Day08(readInput("day08-01-test.txt")).run {
        solve1().also {
            check(it == 2L) { "Invalid: $it" }
            println(it)
        }
    }
    Day08(readInput("day08-02-test.txt")).run {
        solve2().also {
            check(it == 6L) { "Invalid: $it" }
            println(it)
        }
    }

    Day08(readInput("day08.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
