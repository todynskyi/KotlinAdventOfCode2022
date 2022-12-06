fun main() {

    fun calculate(input: String, chunkSize: Int): Int {
        val data = input.toCharArray().toList()
        var start = 0
        var to = chunkSize

        while (to <= data.size) {
            val chunk = data.subList(start, to)
            var index = 0
            while (index <= chunk.size) {
                if (index == chunk.size - 1) {
                    return to
                } else if (chunk.count { it == chunk[index] } > 1) {
                    start += index + 1
                    to += index + 1
                    break
                }
                index++
            }
        }
        return -1
    }

    fun part1(input: String): Int = calculate(input, 4)

    fun part2(input: String): Int = calculate(input, 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test").first()
    println(part1(testInput))
    check(part1(testInput) == 5)
    println(part2(testInput))

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
