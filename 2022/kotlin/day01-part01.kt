import java.io.File

private fun readInput(): List<String> {
    return File("2022/inputs/day01.txt").readLines()
}

fun main() {
    val input = readInput();
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

    println(max);
}
