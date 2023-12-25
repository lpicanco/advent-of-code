package aoc2023

import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph

class Day25(
    private val lines: List<String>,
) {
    fun solve1(): Int {
        val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        lines.forEach { line ->
            val (name, connections) = line.split(": ")
            graph.addVertex(name)
            connections.split(" ").forEach { connection ->
                graph.addVertex(connection)
                graph.addEdge(name, connection)
            }
        }

        val cutSide = StoerWagnerMinimumCut(graph).minCut()
        return cutSide.size * (graph.vertexSet().size - cutSide.size)
    }
}

fun main() {
    Day25(readInput("day25-test.txt")).run {
        solve1().also {
            check(it == 54) { "Invalid: $it" }
            println(it)
        }
    }

    Day25(readInput("day25.txt")).also {
        println(it.solve1())
    }
}
