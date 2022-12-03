fun main() {
    fun List<String>.findCommonChar(): Char = map { it.toSet() }
        .reduce { left, right -> left.intersect(right) }
        .first()

    fun Char.priority(): Int = when (this) {
        in 'a'..'z' -> (this - 'a') + 1
        in 'A'..'Z' -> (this - 'A') + 27
        else -> 0
    }

    fun part1(input: List<String>): Int = input
        .map {
            listOf(
                it.substring(0..it.length / 2),
                it.substring(it.length / 2)
            )
        }
        .map { it.findCommonChar() }.sumOf { it.priority() }

    fun part2(input: List<String>): Int = input.chunked(3)
        .map { it.findCommonChar() }
        .sumOf { it.priority() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    println(part2(testInput))

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
