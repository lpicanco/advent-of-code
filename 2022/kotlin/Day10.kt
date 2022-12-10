class CPU {
    private var tickCount = 1
    var registerX: Int = 1
    val registerXAtTickCount: MutableMap<Int, Int> = mutableMapOf(tickCount to registerX)

    fun run(instruction: String) {
        if (instruction == NOOP_INSTRUCTION) {
            tick()
        } else {
            val (addXAmount) = ADDX_INSTRUCTION.matchEntire(instruction)!!.destructured
            tick()
            registerX += addXAmount.toInt()
            tick()
        }
    }

    private fun tick() {
        tickCount++
        registerXAtTickCount[tickCount] = registerX
    }

    companion object {
        private const val NOOP_INSTRUCTION = "noop"
        private val ADDX_INSTRUCTION = "addx (-?\\d*)".toRegex()
    }
}

private fun part1(input: List<String>): Int {
    val cpu = CPU()
    input.forEach { line ->
        cpu.run(line)
    }

    var signalStrength = 0
    for (i in 20..cpu.registerXAtTickCount.size step 40) {
        signalStrength += i * cpu.registerXAtTickCount.getValue(i)
    }

    return signalStrength
}

private fun part2(input: List<String>) {
    val cpu = CPU()
    input.forEach { line ->
        cpu.run(line)
    }

    for (i in 1 until cpu.registerXAtTickCount.size) {
        val position = cpu.registerXAtTickCount.getValue(i)
        val cursor = i % 40

        if (cursor - 1 in position.minus(1)..position.plus(1)) {
            print("#")
        } else {
            print(".")
        }

        if (cursor == 0) {
            println()
        }
    }
}

fun main() {
    val inputTest = readInput("day10-test.txt")
    check(part1(inputTest) == 13140) { "Invalid: ${part1(inputTest)}" }

    val input = readInput("day10.txt")
    println(part1(input))

    part2(inputTest)
    part2(input)
}
