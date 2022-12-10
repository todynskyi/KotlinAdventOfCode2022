fun main() {

    fun List<Command>.toCycles(): Map<Int, Int> {
        var index = 0
        var x = 1
        val values = mutableMapOf<Int, Int>()

        this.forEach { command ->
            if (command.instruction == "noop") {
                values[++index] = x
            } else {
                values[++index] = x
                values[++index] = x
                x += command.value!!
            }
        }
        return values
    }

    fun part1(input: List<Command>): Int {
        val cycles = input.toCycles()
        return listOf(20, 60, 100, 140, 180, 220).sumOf { (cycles[it] ?: 0) * it }
    }

    fun part2(input: List<Command>): List<String> {
        val cycles = input.toCycles()
        return cycles.map { if ((it.key - 1) % 40 in it.value - 1..it.value + 1) "#" else "." }
            .chunked(40)
            .map { it.joinToString("") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test").toCommands()
    check(part1(testInput) == 13140)
    part2(testInput).forEach { println(it) }

    val input = readInput("Day10").toCommands()
    println(part1(input))
    part2(input).forEach { println(it) }
}

fun List<String>.toCommands(): List<Command> = this.map {
    val parts = it.split(" ")
    Command(parts.first(), parts.getOrNull(1)?.toInt())
}

data class Command(val instruction: String, val value: Int? = null) {}
