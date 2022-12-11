class Monkey(
    private val items: ArrayDeque<Long>,
    private val operation: String,
    val divisibleByTest: Int,
    private val trueAction: String,
    private val falseAction: String
) {
    var inspectedCount = 0L

    fun playRound(monkeys: MutableList<Monkey>, worryLevelAdditionalOperation: (Long) -> Long) {
        while (items.isNotEmpty()) {
            var worryLevel: Long = applyOperation(items.removeFirst())
            worryLevel = worryLevelAdditionalOperation(worryLevel)

            if (runTest(worryLevel)) {
                applyAction(trueAction, worryLevel, monkeys)
            } else {
                applyAction(falseAction, worryLevel, monkeys)
            }
            inspectedCount++
        }
    }

    private fun applyOperation(worryLevel: Long): Long {
        val (_, op, second) = operation.split(" ")

        return when (op) {
            "+" -> worryLevel + (second.toLongOrNull() ?: worryLevel)
            "*" -> worryLevel * (second.toLongOrNull() ?: worryLevel)
            else -> throw IllegalArgumentException("Invalid operation: $operation")
        }
    }

    private fun runTest(worryLevel: Long): Boolean {
        return worryLevel % divisibleByTest == 0L
    }

    private fun applyAction(action: String, worryLevel: Long, monkeys: MutableList<Monkey>) {
        val destination = action.substringAfter("monkey ").toInt()
        monkeys[destination].items.add(worryLevel)
    }

    companion object {
        fun parse(input: List<String>): Monkey {
            return Monkey(
                ArrayDeque(input[1].substringAfter(": ").split(", ").map { it.toLong() }),
                input[2].substringAfter("= "),
                input[3].substringAfter("by ").toInt(),
                input[4].substringAfter(": "),
                input[5].substringAfter(": "),
            )
        }
    }
}

private fun buildMonkeyList(input: List<String>): MutableList<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    for (i in input.indices step 7) {
        monkeys.add(Monkey.parse(input.subList(i, i + 6)))
    }
    return monkeys
}

private fun run(monkeys: MutableList<Monkey>, rounds: Int, worryLevelAdditionalOperation: (Long) -> Long): Long {
    repeat(rounds) {
        monkeys.forEach { it.playRound(monkeys, worryLevelAdditionalOperation) }
    }

    return monkeys.map { it.inspectedCount }.sortedDescending().take(2).reduce(Long::times)
}

private fun part1(input: List<String>): Long {
    val monkeys = buildMonkeyList(input)
    return run(monkeys, 20) { it / 3 }
}

private fun part2(input: List<String>): Long {
    val monkeys = buildMonkeyList(input)
    val modulus = monkeys.map { it.divisibleByTest }.reduce(Int::times)
    return run(monkeys, 10_000) { it % modulus }
}

fun main() {
    val inputTest = readInput("day11-test.txt")
    check(part1(inputTest) == 10605L) { "Invalid: ${part1(inputTest)}" }
    check(part2(inputTest) == 2713310158L) { "Invalid: ${part2(inputTest)}" }

    val input = readInput("day11.txt")

    println(part1(input))
    println(part2(input))
}
