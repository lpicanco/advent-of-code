import java.io.File

fun readInput(filename: String): List<String> {
    return File("2022/inputs", filename).readLines()
}

fun <T> Iterable<T>.sumOfChar(selector: (T) -> Char?): String {
    var sum = ""
    for (element in this) {
        sum += selector(element) ?: ""
    }
    return sum
}
