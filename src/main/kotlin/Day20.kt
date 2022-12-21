fun main() {

    val positions = listOf(1000, 2000, 3000)

    fun decrypt(input: List<Int>, key: Long = 1, times: Int = 1): Long {
        val (initialPositions, encryptedData) = input.mapIndexed { index, i -> index to i * key }
            .let { it to it.toMutableList() }

        repeat(times) {
            initialPositions.forEach { p ->
                val index = encryptedData.indexOf(p)
                encryptedData.removeAt(index)
                encryptedData.add((index + p.second).mod(encryptedData.size), p)
            }
        }
        val index = encryptedData.indexOfFirst { it.second == 0L }
        return positions.sumOf { position -> encryptedData[(index + position) % input.size].second }
    }

    fun part1(input: List<Int>): Long = decrypt(input)

    fun part2(input: List<Int>): Long = decrypt(input, 811589153, 10)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test").map { it.toInt() }
    check(part1(testInput) == 3L)
    println(part2(testInput))

    val input = readInput("Day20").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
