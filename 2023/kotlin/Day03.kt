package aoc2023

class Day03(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        val grid = Grid2D.parse(lines)
        return extractAdjacentNumbers(grid)
            .values.flatten().sum()
    }

    fun solve2(): Int {
        val grid = Grid2D.parse(lines)
        return extractAdjacentNumbers(grid)
            .filter { (key, values) ->
                grid.get(key) == '*' && values.size == 2
            }
            .values.sumOf { it.reduce { acc, i -> acc * i } }
    }

    private fun extractAdjacentNumbers(grid: Grid2D): MutableMap<Position2D, MutableList<Int>> {
        var row = 0
        val numbers = mutableMapOf<Position2D, MutableList<Int>>()

        while (row <= grid.lastRowIndex) {
            var col = 0
            while (col <= grid.lastColIndex) {
                var position = Position2D(row, col)
                var char = grid.get(position)
                var adjacentPos: Position2D? = null
                var num = ""

                while (char.isDigit()) {
                    grid.firstAdjacentOrNull(position) { it != '.' && !it.isDigit() }?.let {
                        adjacentPos = it
                    }

                    num += char
                    col++
                    position = Position2D(row, col)
                    if (!grid.isValid(position)) {
                        break
                    }

                    char = grid.get(position)
                }

                if (adjacentPos != null) {
                    numbers.getOrPut(adjacentPos!!) { mutableListOf() }.add(num.toInt())
                }

                col++
            }
            row++
        }

        return numbers
    }
}

fun main() {
    Day03(readInput("day03-test.txt")).run {
        solve1().also {
            check(it == 4361) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 467835) { "Invalid: $it" }
            println(it)
        }
    }

    Day03(readInput("day03.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
