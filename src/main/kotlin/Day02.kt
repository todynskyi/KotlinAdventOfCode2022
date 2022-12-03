fun main() {

    fun part1(input: List<String>): Int = input.sumOf {
        val player = it[0]
        val you = it[2]

        val yourScore = when (you) {
            'X' -> 1
            'Y' -> 2
            'Z' -> 3
            else -> 0
        }

        val score = when (player) {
            'A' -> {
                when (you) {
                    'X' -> 3
                    'Y' -> 6
                    else -> 0
                }
            }

            'B' -> {
                when (you) {
                    'X' -> 0
                    'Y' -> 3
                    else -> 6
                }
            }

            else -> {
                when (you) {
                    'X' -> 6
                    'Y' -> 0
                    else -> 3
                }
            }
        }
        yourScore + score
    }

    fun part2(input: List<String>): Int = input.sumOf {
        val player = it[0]
        val you = it[2]

        val yourScore = when (you) {
            'X' -> 0
            'Y' -> 3
            'Z' -> 6
            else -> 0
        }

        val score = when (you) {
            'X' -> {
                when (player) {
                    'A' -> 3
                    'B' -> 1
                    'C' -> 2
                    else -> 0
                }
            }
            'Y' -> {
                when (player) {
                    'A' -> 1
                    'B' -> 2
                    'C' -> 3
                    else -> 0
                }
            }
            else -> {
                when (player) {
                    'A' -> 2
                    'B' -> 3
                    'C' -> 1
                    else -> 0
                }
            }
        }
        yourScore + score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    println(part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
