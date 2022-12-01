fun main() {

    fun part1(input: List<List<Long>>): Long = input.asSequence().map { it.sum() }.max()

    fun part2(input: List<List<Long>>): Long = input.asSequence().map { it.sum() }.sortedDescending().take(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readChunkedLongs("Day01_test")
    check(part1(testInput) == 24000L)
    println(part2(testInput))

    val input = readChunkedLongs("Day01")
    println(part1(input))
    println(part2(input))
}
