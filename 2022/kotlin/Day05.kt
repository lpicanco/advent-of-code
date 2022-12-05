private val commandRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()

private fun part1(input: List<String>): String {
    var (crates, lineIndex) = readCrates(input)

    while (lineIndex < input.size) {
        val (moveCount, fromColumn, toColumn) = commandRegex.matchEntire(input[lineIndex])!!.destructured

        repeat(moveCount.toInt()) {
            val crate = crates[fromColumn.toInt() - 1].removeFirst()
            crates[toColumn.toInt() - 1].addFirst(crate)
        }

        lineIndex++
    }

    return crates.sumOfChar { it.firstOrNull() }
}

private fun part2(input: List<String>): String {
    val (crates, nextLineIndex) = readCrates(input)

    for (lineIndex in nextLineIndex until input.size) {
        val (moveCount, fromColumn, toColumn) = commandRegex.matchEntire(input[lineIndex])!!.destructured

        val cratesToMove = (1..moveCount.toInt()).sumOfChar {
            crates[fromColumn.toInt() - 1].removeFirst()
        }.reversed()

        cratesToMove.forEach { crates[toColumn.toInt() - 1].addFirst(it) }
    }

    return crates.sumOfChar { it.firstOrNull() }
}

private fun readCrates(input: List<String>): Pair<List<ArrayDeque<Char>>, Int> {
    val crates = List(10) { ArrayDeque<Char>() }
    var lineIndex = 0

    for (idx in input.indices) {
        val line = input[idx]
        for ((cratesIndex, i) in (1..line.length step 4).withIndex()) {
            val crate = line[i]

            if (crate.isDigit()) {
                lineIndex = idx + 2
                break
            }

            if (crate != ' ') {
                crates[cratesIndex].addLast(crate)
            }
        }
        if (lineIndex > 0) {
            break
        }
    }
    return crates to lineIndex
}

fun main() {
    val inputTest = readInput("day05-test.txt")
    check(part1(inputTest) == "CMZ")
    check(part2(inputTest) == "MCD")

    val input = readInput("day05.txt")
    println(part1(input))
    println(part2(input))
}
