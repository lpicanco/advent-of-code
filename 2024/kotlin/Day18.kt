package aoc2024

import java.util.PriorityQueue

class Day18(
    private val lines: List<String>,
    size: Int,
    private val bytes: Int,
) {
    private val grid =
        Grid2D(
            Array(size) { Array(size) { '.' }.toMutableList() }.toList(),
        )

    init {
        lines.take(bytes).forEach {
            val (x, y) = it.split(",").map { it.toInt() }
            grid.set(Position2D(x, y), '#')
        }
    }

    fun solve1(): Int {
        return dijkstra()
    }

    fun solve2(): String {
        lines.drop(bytes).forEach { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            grid.set(Position2D(x, y), '#')
            if (dijkstra() == -1) {
                return line
            }
        }
        throw IllegalArgumentException("Invalid input")
    }

    data class Node(val position: Position2D, val distance: Int) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            return distance.compareTo(other.distance)
        }
    }

    private fun dijkstra(): Int {
        val start = Position2D(0, 0)
        val end = Position2D(grid.colIndices.last, grid.rowIndices.last)

        val queue = PriorityQueue<Node>()
        val distance = mutableMapOf<Position2D, Int>().withDefault { Int.MAX_VALUE }
        distance[start] = 0
        queue.add(Node(start, 0))

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (node.position == end) {
                return node.distance
            }
            if (node.distance > distance.getValue(node.position)) continue

            for (dir in node.position.orthogonalDirections) {
                val next = node.position.plus(dir)
                if (!grid.isMatch(next, '.')) continue

                val newDist = node.distance + 1
                if (newDist < distance.getValue(next)) {
                    distance[next] = newDist
                    queue.add(Node(next, newDist))
                }
            }
        }
        return -1
    }
}

fun main() {
    Day18(readInput("day18-test.txt"), 7, 12).solve1().also {
        check(it == 22) { "Invalid: $it" }
        println(it)
    }
    Day18(readInput("day18.txt"), 71, 1024).solve1().also {
        println(it)
    }

    Day18(readInput("day18-test.txt"), 7, 12).solve2().also {
        check(it == "6,1") { "Invalid: $it" }
        println(it)
    }
    Day18(readInput("day18.txt"), 71, 1024).solve2().also {
        println(it)
    }
}
