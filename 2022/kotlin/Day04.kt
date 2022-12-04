private fun part1(input: List<String>): Int {
    return input.count { line ->
        val (firstRange, secondRange) = getRanges(line)
        firstRange.fullContains(secondRange) || secondRange.fullContains(firstRange)
    }
}

private fun part2(input: List<String>): Int {
    return input.count { line ->
        val (firstRange, secondRange) = getRanges(line)
        firstRange.contains(secondRange) || secondRange.contains(firstRange)
    }
}

private fun ClosedRange<Int>.contains(another: ClosedRange<Int>): Boolean {
    return another.start in start..endInclusive
}

private fun ClosedRange<Int>.fullContains(another: ClosedRange<Int>): Boolean {
    return start <= another.start && endInclusive >= another.endInclusive
}

private fun getRanges(line: String): List<ClosedRange<Int>> {
    return line.split(",").map {
        val (start, end) = it.split("-")
        start.toInt()..end.toInt()
    }
}

fun main() {
    val inputTest = readInput("day04-test.txt")
    check(part1(inputTest) == 2)
    check(part2(inputTest) == 4)

    val input = readInput("day04.txt")
    println(part1(input))
    println(part2(input))
}
