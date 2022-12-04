private fun part1(input: List<String>): Int {
    var max = 0
    var sum = 0

    for (cal in input) {
        if (cal != "") {
            sum += cal.toInt()
            continue
        }

        if (sum > max) {
            max = sum
        }
        sum = 0
    }

    return max
}

private fun part2(input: List<String>): Int {
    val totals = mutableSetOf<Int>()
    var sum = 0

    for (cal in input) {
        if (cal != "") {
            sum += cal.toInt()
            continue
        }

        totals.add(sum)
        sum = 0
    }

    return totals.sortedDescending().subList(0, 3).sum()
}

fun main() {
    val input = readInput("day01.txt")
    println(part1(input))
    println(part2(input))
}
