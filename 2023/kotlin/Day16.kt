package aoc2023

class Day16(
    lines: List<String>,
) {
    private val grid = Grid2D.parse(lines)
    private val beamQueue = ArrayDeque<Beam>()
    private val seen = mutableSetOf<Pair<Position2D, Direction>>()

    fun solve1(): Int {
        val startBeam = Beam(Position2D(0, 0), Direction.Right)
        return solve(startBeam)
    }

    fun solve2(): Int {
        return listOf(
            grid.rowIndices.flatMap {
                listOf(
                    Beam(Position2D(0, it), Direction.Right),
                    Beam(Position2D(grid.lastColIndex, it), Direction.Right),
                )
            },
            grid.colIndices.flatMap {
                listOf(
                    Beam(Position2D(it, 0), Direction.Down),
                    Beam(Position2D(it, grid.lastRowIndex), Direction.Up),
                )
            },
        ).flatten().maxOf {
            solve(it)
        }
    }

    private fun solve(beam: Beam): Int {
        seen.clear()
        beamQueue.addLast(beam)
        while (beamQueue.isNotEmpty()) {
            val queuedBeam = beamQueue.removeFirst()
            while (moveBeam(queuedBeam)) {
                // move beam
            }
        }
        return seen.distinctBy { it.first }.size
    }

    private fun moveBeam(beam: Beam): Boolean {
        if (!seen.add(beam.position to beam.direction)) {
            return false
        }

        when (val beamValue = grid.get(beam.position)) {
            MIRROR, REVERSE_MIRROR -> {
                interactWithMirror(beam, beamValue)
            }
            SPLIT, REVERSE_SPLIT -> {
                interactWithSplit(beam, beamValue)
            }
        }

        beam.position = beam.position.plus(beam.direction.offset)
        return grid.isValid(beam.position)
    }

    private fun interactWithSplit(
        beam: Beam,
        beamValue: Char,
    ) {
        val newBeamDirection =
            when (beamValue) {
                SPLIT ->
                    when (beam.direction) {
                        Direction.Left, Direction.Right -> Direction.Down to Direction.Up
                        else -> null
                    }
                REVERSE_SPLIT ->
                    when (beam.direction) {
                        Direction.Up, Direction.Down -> Direction.Left to Direction.Right
                        else -> null
                    }
                else -> throw IllegalArgumentException("Invalid split: $beamValue")
            }

        if (newBeamDirection != null) {
            beam.direction = newBeamDirection.first
            beamQueue.addLast(Beam(beam.position, newBeamDirection.second))
        }
    }

    private fun interactWithMirror(
        beam: Beam,
        candidateValue: Char,
    ) {
        val newDirection =
            when (candidateValue) {
                MIRROR ->
                    when (beam.direction) { // /
                        Direction.Right -> Direction.Up
                        Direction.Up -> Direction.Right
                        Direction.Left -> Direction.Down
                        Direction.Down -> Direction.Left
                    }
                REVERSE_MIRROR ->
                    when (beam.direction) { // \
                        Direction.Right -> Direction.Down
                        Direction.Down -> Direction.Right
                        Direction.Left -> Direction.Up
                        Direction.Up -> Direction.Left
                    }
                else -> throw IllegalArgumentException("Invalid mirror: $candidateValue")
            }

        beam.direction = newDirection
    }

    data class Beam(
        var position: Position2D,
        var direction: Direction,
    )

    companion object {
        const val MIRROR = '/'
        const val REVERSE_MIRROR = '\\'
        const val SPLIT = '|'
        const val REVERSE_SPLIT = '-'
    }
}

fun main() {
    Day16(readInput("day16-test.txt")).run {
        solve1().also {
            check(it == 46) { "Invalid: $it" }
            println(it)
        }
        solve2().also {
            check(it == 51) { "Invalid: $it" }
            println(it)
        }
    }
    Day16(readInput("day16.txt")).also {
        println(it.solve1())
        println(it.solve2())
    }
}
