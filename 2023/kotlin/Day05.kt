package aoc2023

class Day05(
    private val lines: List<String>,
) {
    fun solve1(): Long {
        val seeds: Set<Long> = lines[0].substringAfter(": ").split(" ").map { it.toLong() }.toSet()
        return solve(seeds.asSequence())
    }

    fun solve2(): Long {
        // Now seeds are on pairs. First the seed, then the number of seeds
        val seeds =
            lines[0].substringAfter(": ").split(" ").map { it.toLong() }.chunked(2).map { (seed, count) ->
                seed..seed + count
            }
        // transform the list of ranges into a sequence of seeds.
        val seedsSequence = seeds.asSequence().flatMap { it.asSequence() }

        return solve(seedsSequence)
    }

    private fun solve(seeds: Sequence<Long>): Long {
        val maps = mutableMapOf<String, List<RangeMap>>()
        var lineIndex = 2
        while (true) {
            val mapName = lines[lineIndex].substringBefore(" map:")
            val mapLines = lines.drop(lineIndex + 1).takeWhile { it.isNotBlank() }
            val map = parseMap(mapLines)
            maps[mapName] = map
            lineIndex += mapLines.size + 2

            if (lineIndex >= lines.size) {
                break
            }
        }

        return seeds.minOf { seed ->
            val soil = maps[SEED_TO_SOIL]!!.getOrDefault(seed)
            val fertilizer = maps[SOIL_TO_FERTILIZER]!!.getOrDefault(soil)
            val water = maps[FERTILIZER_TO_WATER]!!.getOrDefault(fertilizer)
            val light = maps[WATER_TO_LIGHT]!!.getOrDefault(water)
            val temperature = maps[LIGHT_TO_TEMPERATURE]!!.getOrDefault(light)
            val humidity = maps[TEMPERATURE_TO_HUMIDITY]!!.getOrDefault(temperature)
            maps[HUMIDITY_TO_LOCATION]!!.getOrDefault(humidity)
        }
    }

    private fun parseMap(mapLines: List<String>): List<RangeMap> {
        return mapLines.map { line ->
            val (destination, source, length) = line.split(" ").map { it.toLong() }
            RangeMap(destination, source, length)
        }
    }

    data class RangeMap(private val startDestinationRange: Long, private val startSourceRange: Long, private val rangeLength: Long) {
        private val sourceRange = startSourceRange..startSourceRange + rangeLength

        fun getOrNull(source: Long): Long? {
            if (source !in sourceRange) {
                return null
            }
            return startDestinationRange + (source - startSourceRange)
        }
    }

    private fun List<RangeMap>.getOrDefault(source: Long): Long {
        for (map in this) {
            map.getOrNull(source)?.let {
                return it
            }
        }
        return source
    }

    companion object {
        const val SEED_TO_SOIL = "seed-to-soil"
        const val SOIL_TO_FERTILIZER = "soil-to-fertilizer"
        const val FERTILIZER_TO_WATER = "fertilizer-to-water"
        const val WATER_TO_LIGHT = "water-to-light"
        const val LIGHT_TO_TEMPERATURE = "light-to-temperature"
        const val TEMPERATURE_TO_HUMIDITY = "temperature-to-humidity"
        const val HUMIDITY_TO_LOCATION = "humidity-to-location"
    }
}

fun main() {
    Day05(readInput("day05-test.txt")).run {
        solve1().also {
            check(it == 35L) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 46L) { "Invalid: $it" }
            println(it)
        }
    }

    Day05(readInput("day05.txt")).also {
        println(it.solve1())
        println(it.solve2() - 1)
    }
}
