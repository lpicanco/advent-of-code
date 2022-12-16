class Day14(private val input: List<String>) {
    private lateinit var screen: List<MutableList<Char>>
    private var sandCount = 0
    private var activeSand = spawnSand()
    private lateinit var screenBuffer: List<MutableList<Char>>

    private fun spawnSand() = Position(0, 500)

    private var end = false

    private fun initializeRocks(additionalTiles: Int = 0) {
        var rockList = input.map {
            it.split(" -> ").windowed(size = 2, step = 1).map { path ->
                val (fromCol, fromRow) = path[0].split(",")
                val (toCol, toRow) = path[1].split(",")
                Position(fromRow.toInt(), fromCol.toInt()) to Position(toRow.toInt(), toCol.toInt())
            }
        }

        val positions = rockList.flatMap { it.flatMap { it.toList() } }
        screenBuffer = List(positions.maxOf { it.row } + 1 + additionalTiles) { MutableList(positions.maxOf { it.col } + 200) { ' ' } }

        if (additionalTiles > 0) {
            rockList = rockList + listOf(
                listOf(Position(screenBuffer.size - 1, 300) to Position(screenBuffer.size - 1, screenBuffer[0].size - 1))
            )
        }

        // simulate rocks
        rockList.forEach { path ->
            path.forEach { (from, to) ->
                for (row in minOf(from.row, to.row)..maxOf(from.row, to.row)) {
                    for (col in minOf(from.col, to.col)..maxOf(from.col, to.col)) {
                        screenBuffer[row][col] = '#'
                    }
                }
            }
        }
    }

    private fun simulate() {
        screen = screenBuffer.map { it.toMutableList() }

        if (screen[activeSand.row + 1][activeSand.col] == ' ') {
            activeSand.row++
        } else if (screen[activeSand.row + 1][activeSand.col - 1] == ' ') {
            activeSand.row++
            activeSand.col--
        } else if (screen[activeSand.row + 1][activeSand.col + 1] == ' ') {
            activeSand.row++
            activeSand.col++
        } else {
            sandCount++
            screenBuffer.set(activeSand, 'o')
            screen.set(activeSand, 'o')

            if (activeSand.row == spawnSand().row) {
                end = true
            }
            activeSand = spawnSand()
        }

        screen.set(activeSand, 'o')

        if (activeSand.row == screen.size - 1) {
            end = true
        }
    }

    private fun render() {
        var begin = false

        screen.forEach { row ->
            if (row.joinToString("").isNotBlank()) {
                begin = true
            }

            if (begin) {
                println(row.joinToString("").substring(290))
            }
        }
    }

    fun solve1(): Int {
        var count = 0
        initializeRocks()

        while (!end) {
            simulate()
            if (count % 10_000 == 0) {
                render()
            }
            count++
        }

        render()
        return sandCount
    }

    fun solve2(): Int {
        var count = 0
        initializeRocks(2)

        while (!end) {
            simulate()
            if (count % 100_000 == 0) {
                render()
            }
            count++
        }

        render()
        return sandCount
    }
}

fun main() {
    Day14(readInput("day14-test.txt")).solve1().also {
        check(it == 24) { "Invalid: $it" }
    }
    Day14(readInput("day14-test.txt")).solve2().also {
        check(it == 93) { "Invalid: $it" }
    }

    println(Day14(readInput("day14.txt")).solve1())
    println(Day14(readInput("day14.txt")).solve2())
}
