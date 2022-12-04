enum class Shape(private val shapePoints: Int, private val winAgainst: Int) {
    Rock(1, 3),
    Paper(2, 1),
    Scissors(3, 2);

    fun outcome(another: Shape): Int = when {
        this == another -> 3
        winAgainst == another.shapePoints -> 6
        else -> 0
    }

    fun play(another: Shape): Int {
        return outcome(another) + shapePoints
    }

    companion object {
        fun parse(value: String): Shape = when (value) {
            "A", "X" -> Rock
            "B", "Y" -> Paper
            "C", "Z" -> Scissors
            else -> throw IllegalArgumentException("Invalid shape: $value")
        }
    }
}

private fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        val (opponent, me) = line.split(" ").map { Shape.parse(it) }
        me.play(opponent)
    }
}

private fun part2(input: List<String>): Int {
    return input.sumOf { line ->
        val (opponent, me) = line.split(" ").map { Shape.parse(it) }

        val trueMe = when (me) {
            Shape.Rock -> Shape.values().first { it.outcome(opponent) == 0 } // Lose
            Shape.Paper -> Shape.values().first { it.outcome(opponent) == 3 } // Draw
            Shape.Scissors -> Shape.values().first { it.outcome(opponent) == 6 } // Win
        }

        trueMe.play(opponent)
    }
}

fun main() {
    val input = readInput("day02.txt")
    println(part1(input))
    println(part2(input))
}
