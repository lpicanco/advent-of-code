package aoc2024

class Day14(
    lines: List<String>,
    private val width: Int,
    private val height: Int,
) {
    data class Robot(
        var position: Position2D,
        val velocity: Position2D,
    )

    private val robots: MutableList<Robot> =
        lines.map { l ->
            val (posX, posY) = l.substringAfter("=").substringBefore(" ").split(",").map { it.toInt() }
            val (velX, velY) = l.substringAfter("v=").split(",").map { it.toInt() }
            Robot(Position2D(posX, posY), Position2D(velX, velY))
        }.toMutableList()

    fun solve1(): Int {
        repeat(100) {
            simulate()
        }

        val quadrants = Array(4) { 0 }
        val midX = width / 2
        val midY = height / 2
        robots.forEach { robot ->
            if (robot.position.x < midX && robot.position.y < midY) quadrants[0]++
            if (robot.position.x > midX && robot.position.y < midY) quadrants[1]++
            if (robot.position.x < midX && robot.position.y > midY) quadrants[2]++
            if (robot.position.x > midX && robot.position.y > midY) quadrants[3]++
        }
        return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3]
    }

    fun solve2(): Int {
        for (i in 1 until Int.MAX_VALUE) {
            simulate()
            if (robots.groupBy { it.position }.values.all { it.size == 1 }) {
                return i
            }
        }
        return 0
    }

    private fun simulate() {
        robots.forEach { robot ->
            var x = (robot.position.x + robot.velocity.x) % width
            var y = (robot.position.y + robot.velocity.y) % height
            if (x < 0) x += width
            if (y < 0) y += height
            robot.position = Position2D(x, y)
        }
    }
}

fun main() {
    Day14(readInput("day14-test.txt"), 11, 7).solve1().also {
        check(it == 12) { "Invalid: $it" }
        println(it)
    }
    Day14(readInput("day14.txt"), 101, 103).solve1().also {
        println(it)
    }

    Day14(readInput("day14.txt"), 101, 103).solve2().also {
        println(it)
    }
}
