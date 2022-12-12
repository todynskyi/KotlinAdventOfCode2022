fun main() {

    fun calculate(monkeys: List<Monkey>, numberOfRounds: Int, worryLevel: (Long) -> Long): Long {
        val inspections = monkeys.associate { it.name to MonkeyInspections(it.name, it.items) }
            .toMutableMap()

        val rounds = mutableMapOf<Int, Map<String, MonkeyInspections>>()

        (1..numberOfRounds).forEach { round ->
            monkeys.forEach { monkey ->
                val monkeyInspections = inspections[monkey.name]!!
                val items = monkeyInspections.items

                var count = monkeyInspections.count
                items.forEach { item ->
                    count++
                    val value = when (monkey.operation.type) {
                        Operation.Type.MULTIPLE -> item * monkey.operation.value!!
                        Operation.Type.PLUS -> item + monkey.operation.value!!
                        else -> item * item
                    }

                    val level = worryLevel(value)
                    val toMonkeyName = if (level % monkey.divisible.toLong() == 0L) {
                        monkey.trueMonkey
                    } else {
                        monkey.falseMonkey
                    }

                    val toMonkey = inspections[toMonkeyName]!!
                    inspections[toMonkeyName] = toMonkey.copy(items = toMonkey.items + level)
                }
                inspections[monkey.name] = monkeyInspections.copy(items = emptyList(), count = count)
                rounds[round] = inspections.toMap()
            }
        }

        return inspections.values
            .sortedByDescending { it.count }
            .take(2)
            .map { it.count }
            .reduce(Long::times)
    }

    fun part1(input: List<Monkey>): Long = calculate(input, 20) { it / 3 }

    fun part2(input: List<Monkey>): Long {
        val divisible = input.map { it.divisible }.reduce(Int::times)
        return calculate(input, 10000) {
            it % divisible
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test").toMonkeys()
    check(part1(testInput) == 10605L)
    println(part2(testInput))

    val input = readInput("Day11").toMonkeys()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toMonkeys(): List<Monkey> {
    return this.filter { it.isNotBlank() }
        .chunked(6)
        .map { monkey ->
            val name = monkey[0].substringBefore(":").lowercase()
            val items = monkey[1].substringAfter(": ").split(",").toList().map { it.trim().toLong() }
            val operationRaw = monkey[2].substringAfter(" Operation: new = ")
            val operation = when {
                operationRaw == "old * old" -> {
                    Operation(Operation.Type.DOUBLE)
                }

                operationRaw.startsWith("old +") -> {
                    Operation(Operation.Type.PLUS, operationRaw.substringAfter("old + ").toLong())
                }

                operationRaw.startsWith("old *") -> {
                    Operation(Operation.Type.MULTIPLE, operationRaw.substringAfter("old * ").toLong())
                }

                else -> {
                    error("Not supported: $operationRaw")
                }
            }
            val divisible = monkey[3].substringAfter("Test: divisible by ").toInt()
            val trueMonkey = monkey[4].substringAfter("If true: throw to ")
            val falseMonkey = monkey[5].substringAfter("If false: throw to ")
            Monkey(name, items, operation, divisible, trueMonkey, falseMonkey)
        }
}

data class Monkey(
    val name: String,
    val items: List<Long>,
    val operation: Operation,
    val divisible: Int,
    val trueMonkey: String,
    val falseMonkey: String
)

data class Operation(val type: Type, val value: Long? = null) {
    enum class Type {
        PLUS,
        MULTIPLE,
        DOUBLE
    }
}

data class MonkeyInspections(val name: String, val items: List<Long>, val count: Long = 0L)
