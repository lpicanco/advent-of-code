class Day22(private val input: List<String>) {
    enum class Direction(val score: Int, val offset: Position) {
        R(0, Position(0, 1)), D(1, Position(1, 0)), L(2, Position(0, -1)), U(3, Position(-1, 0))
    }

    private val map: List<List<Char>> = input.takeWhile { it.length > 1 }.map {
        val list = MutableList(150) { ' ' }

        it.toCharArray().mapIndexed { index: Int, c: Char -> list[index] = c }
        list
    }

    private fun parseInstructions(inputLine: String): Sequence<String> {
        var index = 0
        return generateSequence {
            if (index >= inputLine.length) {
                return@generateSequence null
            }

            if (!inputLine[index].isDigit()) {
                return@generateSequence inputLine[index++].toString()
            }

            var buffer = ""
            while (index < inputLine.length && inputLine[index].isDigit()) {
                buffer += inputLine[index++].toString()
            }

            return@generateSequence buffer
        }
    }

    private fun solve(plane: Boolean): Int {
        val instructions = parseInstructions(input.last())
        var (position, direction) = move(Position(0, 0), Direction.R, plane)
        instructions.forEach { inst ->
            val steps = inst.toIntOrNull()
            if (steps != null) {
                repeat(steps) {
                    move(position, direction, plane).let {
                        position = it.first
                        direction = it.second
                    }
                }
            } else {
                direction = if (inst == "L") {
                    if (direction == Direction.R) Direction.U
                    else Direction.values()[direction.ordinal - 1]
                } else {
                    if (direction == Direction.U) Direction.R
                    else Direction.values()[direction.ordinal + 1]
                }
            }
        }
        return (position.row.plus(1) * 1000) + (position.col.plus(1) * 4) + direction.score
    }

    fun solve1(): Int {
        return solve(plane = true)
    }

    private fun move(position: Position, direction: Direction, plane: Boolean): Pair<Position, Direction> {
        var newPosition = position
        var newDirection = direction

        do {
            if (plane || getFace(newPosition) == null) {
                movePlan(newPosition, newDirection)
            } else {
                moveCube(newPosition, newDirection)
            }.let {
                newPosition = it.first
                newDirection = it.second
            }

            if (map.get(newPosition) == WALL) {
                return position to direction
            }
        } while (map.get(newPosition) != SPACE)

        return newPosition to newDirection
    }

    private fun movePlan(position: Position, direction: Direction): Pair<Position, Direction> {
        val candidatePosition = position + direction.offset

        if (candidatePosition.row >= map.size) {
            candidatePosition.row = 0
        }

        if (candidatePosition.row < 0) {
            candidatePosition.row = map.size - 1
        }

        if (candidatePosition.col >= map[candidatePosition.row].size) {
            candidatePosition.col = 0
        }

        if (candidatePosition.col < 0) {
            candidatePosition.col = map[candidatePosition.row].size - 1
        }
        return candidatePosition to direction
    }

    private fun moveCube(position: Position, direction: Direction): Pair<Position, Direction> {
        val currentFace = getFace(position)!!

        val candidatePosition = position + direction.offset
        val candidateFace = getFace(candidatePosition)

        if (currentFace == candidateFace) {
            return candidatePosition to direction
        }

        return when (direction) {
            Direction.R -> currentFace.moveRight(position, candidatePosition) to currentFace.rightDirection
            Direction.D -> currentFace.moveDown(position, candidatePosition) to currentFace.downDirection
            Direction.L -> currentFace.moveLeft(position, candidatePosition) to currentFace.leftDirection
            Direction.U -> currentFace.moveUp(position, candidatePosition) to currentFace.upDirection
        }
    }

    fun solve2(): Int {
        return solve(plane = false)
    }

    private fun getFace(position: Position): Face? {
        return faces.firstOrNull {
            position.row >= it.from.row && position.row <= it.to.row &&
                position.col >= it.from.col && position.col <= it.to.col
        }
    }

    data class Face(
        val from: Position,
        val to: Position,
        val rightDirection: Direction,
        val downDirection: Direction,
        val leftDirection: Direction,
        val upDirection: Direction
    ) {
        lateinit var rightFace: Face
        lateinit var downFace: Face
        lateinit var leftFace: Face
        lateinit var upFace: Face

        fun moveRight(position: Position, candidatePosition: Position): Position {
            return when (rightDirection) {
                Direction.L -> return Position(rightFace.to.row - (position.row - from.row), rightFace.to.col)
                Direction.U -> return Position(rightFace.to.row, rightFace.from.col + (position.row - from.row))
                else -> candidatePosition
            }
        }

        fun moveLeft(position: Position, candidatePosition: Position): Position {
            return when (leftDirection) {
                Direction.R -> return Position(leftFace.to.row - (position.row - from.row), leftFace.from.col)
                Direction.D -> return Position(leftFace.from.row, leftFace.from.col + (position.row - from.row))
                else -> candidatePosition
            }
        }

        fun moveDown(position: Position, candidatePosition: Position): Position {
            return when (downDirection) {
                Direction.L -> return Position(downFace.from.row + (position.col - from.col), downFace.to.col)
                Direction.D -> return Position(downFace.from.row, downFace.from.col + (position.col - from.col))
                else -> candidatePosition
            }
        }

        fun moveUp(position: Position, candidatePosition: Position): Position {
            return when (upDirection) {
                Direction.R -> return Position(upFace.from.row + (position.col - from.col), upFace.from.col)
                Direction.U -> return Position(upFace.to.row, upFace.from.col + (position.col - from.col))
                else -> candidatePosition
            }
        }
    }

    companion object {
        private const val WALL = '#'
        private const val SPACE = '.'
        val faces = run {
            val face01 = Face(
                Position(0, 50),
                Position(49, 99),
                rightDirection = Direction.R, // 2
                downDirection = Direction.D, // 3
                leftDirection = Direction.R, // 4
                upDirection = Direction.R, // 6
            )
            val face02 = Face(
                Position(0, 100),
                Position(49, 149),
                rightDirection = Direction.L, // 5
                downDirection = Direction.L, // 3
                leftDirection = Direction.L, // 1
                upDirection = Direction.U, // 6
            )
            val face03 = Face(
                Position(50, 50),
                Position(99, 99),
                rightDirection = Direction.U, // 3
                downDirection = Direction.D, // 5
                leftDirection = Direction.D, // 4
                upDirection = Direction.U, // 1
            )
            val face04 = Face(
                Position(100, 0),
                Position(149, 49),
                rightDirection = Direction.R, // 5
                downDirection = Direction.D, // 6
                leftDirection = Direction.R, // 1
                upDirection = Direction.R, // 3
            )
            val face05 = Face(
                Position(100, 50),
                Position(149, 99),
                rightDirection = Direction.L, // 2
                downDirection = Direction.L, // 6
                leftDirection = Direction.L, // 4
                upDirection = Direction.U, // 3
            )
            val face06 = Face(
                Position(150, 0),
                Position(199, 49),
                rightDirection = Direction.U, // 5
                downDirection = Direction.D, // 2
                leftDirection = Direction.D, // 1
                upDirection = Direction.U, // 4
            )

            face01.rightFace = face02
            face01.downFace = face03
            face01.leftFace = face04
            face01.upFace = face06

            face02.rightFace = face05
            face02.downFace = face03
            face02.leftFace = face01
            face02.upFace = face06

            face03.rightFace = face02
            face03.downFace = face05
            face03.leftFace = face04
            face03.upFace = face01

            face04.rightFace = face05
            face04.downFace = face06
            face04.leftFace = face01
            face04.upFace = face03

            face05.rightFace = face02
            face05.downFace = face06
            face05.leftFace = face04
            face05.upFace = face03

            face06.rightFace = face05
            face06.downFace = face02
            face06.leftFace = face01
            face06.upFace = face04
            listOf(face01, face02, face03, face04, face05, face06)
        }
    }
}

fun main() {
    val solution = Day22(readInput("day22.txt"))
    println(solution.solve1())
    println(solution.solve2())
}
