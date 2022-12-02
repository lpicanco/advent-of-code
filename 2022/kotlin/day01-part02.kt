import java.io.File

private fun readInput(): List<String> {
    return File("2022/inputs/day01.txt").readLines()
}

fun main() {
    val input = readInput()
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

    println(totals.sortedDescending().subList(0, 3))
    println(totals.sortedDescending().subList(0, 3).sum())
}
