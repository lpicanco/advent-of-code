private const val lowerOffset = 96 // A == 97
private const val upperOffset = 38 // a == 65

private val charMap: Map<Char, Int> = (
    ('a'..'z').map { it to it.code - lowerOffset } + ('A'..'Z').map { it to it.code - upperOffset }
    ).toMap()

private fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        val firstCompartment = line.take(line.length / 2).toSet()
        val secondCompartment = line.drop(line.length / 2).toSet()

        firstCompartment.intersect(secondCompartment).sumOf {
            charMap.getValue(it)
        }
    }
}

private fun part2(input: List<String>): Int {
    var sum = 0
    for (i in input.indices step 3) {
        sum += input[i].toSet()
            .intersect(input[i + 1].toSet())
            .intersect(input[i + 2].toSet())
            .sumOf { charMap.getValue(it) }
    }

    return sum
}

fun main() {
    val inputTest = readInput("day03-test.txt")
    check(part1(inputTest) == 157)
    check(part2(inputTest) == 70)

    val input = readInput("day03.txt")
    println(part1(input))
    println(part2(input))
}
