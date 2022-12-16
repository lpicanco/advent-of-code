import kotlin.math.abs

class Day15(private val input: List<String>) {
    data class SensorBeacon(val sensor: Position, val beacon: Position)

    private val sensorBeacons: Set<SensorBeacon> = parseInput()

    private fun parseInput(): Set<SensorBeacon> {
        return input.map {
            val (sensorX, sensorY, beaconX, beaconY) = sensorBeaconRegex.matchEntire(it)!!.destructured
            SensorBeacon(Position(sensorY.toInt(), sensorX.toInt()), Position(beaconY.toInt(), beaconX.toInt()))
        }.toSet()
    }

    private fun getBlockedRange(sensorBeacon: SensorBeacon, row: Int): IntRange? {
        val sensorBeaconDistance: Int = sensorBeacon.sensor.manhattanDistance(sensorBeacon.beacon)

        val distanceToSensor = abs(sensorBeacon.sensor.row - row)
        return if (distanceToSensor <= sensorBeaconDistance) {
            sensorBeacon.sensor.col - sensorBeaconDistance + distanceToSensor..sensorBeacon.sensor.col + sensorBeaconDistance - distanceToSensor
        } else null
    }

    fun solve1(rowToCheck: Int): Int {
        val blockedRanges = mutableSetOf<IntRange>()

        sensorBeacons.forEach { sensorBeacon ->
            getBlockedRange(sensorBeacon, rowToCheck)?.also {
                blockedRanges.add(it)
            }
        }

        val beaconsAtTarget = sensorBeacons.filter { it.beacon.row == rowToCheck }.distinctBy { it.beacon }.size
        return blockedRanges.flatten().distinct().size - beaconsAtTarget
    }

    fun solve2(): Long {
        repeat(highLimit) { y ->
            val ranges = sensorBeacons.mapNotNull { getBlockedRange(it, y) }.sortedBy { it.first }

            var reducedRange = ranges[0]

            for (range in ranges) {
                if (reducedRange.first <= range.first && reducedRange.last >= range.last) {
                    continue
                }

                if (reducedRange.contains(range.first) || reducedRange.contains(range.last) || range.contains(reducedRange.first)) {
                    reducedRange = minOf(reducedRange.first, range.first)..maxOf(reducedRange.last, range.last)
                    continue
                }

                return (highLimit.toLong() * (reducedRange.last + 1)) + y
            }
        }

        return -1
    }

    companion object {
        private val sensorBeaconRegex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
        private const val highLimit = 4_000_000
    }
}

fun main() {
    val testSolution = Day15(readInput("day15-test.txt"))

    testSolution.solve1(10).also {
        check(it == 26) { "Invalid: $it" }
    }
    testSolution.solve2().also {
        check(it == 56000011L) { "Invalid: $it" }
    }

    val solution = Day15(readInput("day15.txt"))
    println(solution.solve1(2_000_000))
    println(solution.solve2())
}
