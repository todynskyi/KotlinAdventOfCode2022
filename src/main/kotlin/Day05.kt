import java.util.*

fun main() {

    fun parse(input: List<String>): Pair<MutableMap<Int, LinkedList<String>>, List<MoveCommand>> {
        val data = input.takeWhile { it.isNotBlank() }
        val numberOfStacks =
            data.last().mapIndexedNotNull { index, c -> if (c.isDigit()) c.toString().toInt() to index else null }

        val stacks = mutableMapOf<Int, LinkedList<String>>()

        data.dropLast(1).forEach { line ->
            numberOfStacks.forEach { (stack, charIndex) ->
                val value = line.getOrNull(charIndex)?.toString()
                if (!value.isNullOrBlank()) {
                    stacks[stack] = stacks.getOrDefault(stack, LinkedList<String>()).also { it.add(value) }
                }
            }
        }

        val commands = input.subList(data.size + 1, input.size).map {
            val parts = it.split(" ")
            MoveCommand(amount = parts[1].toInt(), from = parts[3].toInt(), to = parts.last().toInt())
        }
        return stacks to commands
    }

    fun Map<Int, List<String>>.toMessage(): String = this.map { it.key to it.value.first() }
        .sortedBy { it.first }
        .mapNotNull { it.second }
        .joinToString("")

    fun part1(input: List<String>): String {
        val (stacks, commands) = parse(input)
        commands.forEach { command ->
            val from = stacks[command.from]!!
            val to = stacks[command.to]!!
            repeat(command.amount) {
                val value = from.pollFirst()
                to.addFirst(value)
            }
        }

        return stacks.toMessage()
    }

    fun part2(input: List<String>): String {
        val (stacks, commands) = parse(input)
        commands.forEach { command ->
            val from = stacks[command.from]!!
            val to = stacks[command.to]!!
            val data = mutableListOf<String>()
            repeat(command.amount) {
                data.add(from.pollFirst())
            }
            data.reversed().forEach {
                to.addFirst(it)
            }
        }

        return stacks.toMessage()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    println(part2(testInput))

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class MoveCommand(val amount: Int, val from: Int, val to: Int)
