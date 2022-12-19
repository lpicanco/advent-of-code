import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private typealias ResourceType = String

class Day19(input: List<String>) {
    data class RobotBlueprint(val resourceType: ResourceType, val cost: Map<ResourceType, Int>) {
        fun canBuild(resources: Map<ResourceType, Int>): Boolean {
            return cost.all { (k, v) -> resources.getOrDefault(k, 0) >= v }
        }

        fun build(resources: Map<ResourceType, Int>): MutableMap<ResourceType, Int> {
            val resourceLeft = resources.toMutableMap()
            cost.forEach { (k, vCost) -> resourceLeft.compute(k) { _, v -> v!! - vCost } }
            return resourceLeft
        }
    }
    data class Blueprint(val id: Int, val robots: List<RobotBlueprint>)

    private val bluePrints: List<Blueprint> = parseInput(input)

    private fun parseInput(input: List<String>) = input.map { line ->
        Blueprint(
            bluePrintRegex.matchEntire(line)!!.groupValues[1].toInt(),
            line.split(". ").map { robot ->
                robotRegex.matchEntire(robot)!!.groupValues.let {
                    val cost = mutableMapOf(it[3] to it[2].toInt())
                    if (it[6].isNotBlank()) {
                        cost[it[6]] = it[5].toInt()
                    }

                    RobotBlueprint(it[1], cost)
                }
            }
        )
    }

    private fun solve(blueprint: Blueprint, minuteCount: Int): Int {
        data class QueueItem(val robots: Map<ResourceType, Int>, val resources: Map<ResourceType, Int>, val minuteCount: Int = 1)

        val queue = ArrayDeque(listOf(QueueItem(mapOf("ore" to 1), mutableMapOf())))
        var maxGeode = 0
        val bestGeodeBot = mutableMapOf<Int, QueueItem>()
        val bestGeodeResource = mutableMapOf<Int, Map<ResourceType, Int>>()
        val maxCost: Map<ResourceType, Int> = blueprint.robots.flatMap { it.cost.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.max() * 1 }

        while (queue.isNotEmpty()) {
            val queueItem = queue.removeLast()

            val newResources = queueItem.resources.toMutableMap()
            queueItem.robots.forEach { (r, quantity) ->
                newResources.compute(r) { _, v -> (v ?: 0) + quantity }
            }
            val geode = newResources["geode"] ?: 0

            if (queueItem.minuteCount == minuteCount) {
                if (geode > maxGeode) {
                    println("${blueprint.id}: ${queueItem.robots} - [$newResources] ${queue.size}")
                }
                maxGeode = maxOf(maxGeode, geode)

                continue
            }

            if ((bestGeodeBot[queueItem.minuteCount]?.robots?.get("geode") ?: 0) > queueItem.robots.getOrDefault("geode", 0)) {
                continue
            } else {
                bestGeodeBot[queueItem.minuteCount] = queueItem
            }

            if ((bestGeodeResource[queueItem.minuteCount]?.get("geode") ?: 0) > geode) {
                continue
            } else {
                bestGeodeResource[queueItem.minuteCount] = newResources
            }

            queue.addLast(QueueItem(queueItem.robots, newResources, queueItem.minuteCount + 1))
            blueprint.robots.forEach { robot ->
                if (robot.resourceType == "geode" || queueItem.robots.getOrDefault(robot.resourceType, 0) <= maxCost[robot.resourceType]!!) {
                    if (robot.canBuild(queueItem.resources)) {
                        val newRobots = queueItem.robots.toMutableMap().also { it.compute(robot.resourceType) { _, v -> (v ?: 0) + 1 } }
                        queue.addLast(QueueItem(newRobots, robot.build(newResources), queueItem.minuteCount + 1))
                    }
                }
            }
        }
        println("${blueprint.id} - maxGeode: $maxGeode")
        return maxGeode
    }

    fun solve1(): Int = runBlocking {
        withContext(Dispatchers.Default) {
            bluePrints.asFlow().flatMapMerge(1) {
                flow { emit(solve(it, 24) * it.id) }
            }.toList().reduce(Int::plus)
        }
    }

    fun solve2(): Int = runBlocking {
        withContext(Dispatchers.Default) {
            bluePrints.take(3).asFlow().flatMapMerge(5) {
                flow { emit(solve(it, 32)) }
            }.toList().reduce(Int::times)
        }
    }

    companion object {
        private val robotRegex = Regex(".*Each (\\w+) .*costs (\\d+) (\\w+)( and )?(\\d+)? ?(\\w+)?.*?")
        private val bluePrintRegex = Regex("Blueprint (\\d+).*")
    }
}

fun main() {
    val testSolution = Day19(readInput("day19-test.txt"))

    testSolution.solve1().also {
        check(it == 33) { "Invalid: $it" }
    }

    testSolution.solve2().also {
        check(it == 3348) { "Invalid: $it" }
    }

    val solution = Day19(readInput("day19.txt"))
    println(solution.solve1())
    println(solution.solve2())
}
