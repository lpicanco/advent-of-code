class Day18(input: List<String>) {
    private val cubes: Set<Vector3> = parseInput(input)

    private fun parseInput(input: List<String>) = input.map {
        val (x, y, z) = it.split(",")
        Vector3(x.toInt(), y.toInt(), z.toInt())
    }.toSet()

    private fun calculateNeighbors(cube: Vector3): Set<Vector3> {
        return setOf(
            Vector3(cube.x - 1, cube.y, cube.z),
            Vector3(cube.x + 1, cube.y, cube.z),
            Vector3(cube.x, cube.y - 1, cube.z),
            Vector3(cube.x, cube.y + 1, cube.z),
            Vector3(cube.x, cube.y, cube.z - 1),
            Vector3(cube.x, cube.y, cube.z + 1)
        )
    }
    private fun countNeighbors(cube: Vector3): Int {
        return calculateNeighbors(cube).count { it in cubes }
    }

    fun solve1(): Int {
        return cubes.sumOf { 6 - countNeighbors(it) }
    }

    fun solve2(): Int {
        val minCube = Vector3(cubes.minOf { it.x } - 1, cubes.minOf { it.y } - 1, cubes.minOf { it.z } - 1)
        val maxCube = Vector3(cubes.maxOf { it.x } + 1, cubes.maxOf { it.y } + 1, cubes.maxOf { it.z } + 1)
        var area = 0

        val queue = ArrayDeque(listOf(minCube))
        val visited = mutableSetOf(minCube)
        while (queue.isNotEmpty()) {
            val cube = queue.removeFirst()

            calculateNeighbors(cube)
                .filter { !visited.contains(it) }
                .filter { it.x >= minCube.x && it.y >= minCube.y && it.z >= minCube.z }
                .filter { it.x <= maxCube.x && it.y <= maxCube.y && it.z <= maxCube.z }
                .forEach { nb ->
                    if (nb in cubes) {
                        area++
                    } else {
                        queue.addLast(nb)
                        visited.add(nb)
                    }
                }
        }

        return area
    }
}

fun main() {
    val testSolution = Day18(readInput("day18-test.txt"))

    testSolution.solve1().also {
        check(it == 64) { "Invalid: $it" }
    }

    testSolution.solve2().also {
        check(it == 58) { "Invalid: $it" }
    }

    val solution = Day18(readInput("day18.txt"))

    println(solution.solve1())
    println(solution.solve2()) // 2520
}
