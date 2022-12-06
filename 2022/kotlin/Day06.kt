private fun part1(input: String): Int {
    return detectMarker(input, 4)
}

private fun part2(input: String): Int {
    return detectMarker(input, 14)
}

private fun detectMarker(input: String, uniqueCharCount: Int): Int {
    for (i in 0 until input.length - uniqueCharCount) {
        val chars = input.substring(i, i + uniqueCharCount).toSet()
        if (chars.size == uniqueCharCount) {
            return i + uniqueCharCount
        }
    }

    return -1
}

fun main() {
    val inputTest = readInput("day06-test.txt")
    check(part1(inputTest[0]) == 7)
    check(part1(inputTest[1]) == 5)
    check(part1(inputTest[2]) == 6)
    check(part1(inputTest[3]) == 10)
    check(part1(inputTest[4]) == 11)
    check(part2(inputTest[0]) == 19)
    check(part2(inputTest[1]) == 23)
    check(part2(inputTest[2]) == 23)
    check(part2(inputTest[3]) == 29)
    check(part2(inputTest[4]) == 26)

    val input = readInput("day06.txt")
    println(part1(input[0]))
    println(part2(input[0]))
}
