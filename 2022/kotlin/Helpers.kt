import java.io.File

fun readInput(filename: String): List<String> {
    return File("2022/inputs", filename).readLines()
}
