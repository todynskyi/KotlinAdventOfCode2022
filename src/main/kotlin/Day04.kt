fun main() {

    fun calculate(input: List<String>, isOverlap: (IntRange, IntRange) -> Boolean): Int = input.map {
        val ranges = it.split(",")
        val leftRange = ranges[0].split("-").let { range -> range[0].toInt()..range[1].toInt() }
        val rightRange = ranges[1].split("-").let { range -> range[0].toInt()..range[1].toInt() }

        if (isOverlap(leftRange, rightRange)) 1 else 0
    }.sum()

    fun part1(input: List<String>): Int = calculate(input) { leftRange, rightRange ->
        (leftRange.contains(rightRange.first) && leftRange.contains(rightRange.last)) ||
                rightRange.contains(leftRange.first) && rightRange.contains(leftRange.last)
    }

    fun part2(input: List<String>): Int = calculate(input) { leftRange, rightRange ->
        leftRange.contains(rightRange.first) || leftRange.contains(rightRange.last) ||
                rightRange.contains(leftRange.first) || rightRange.contains(leftRange.last)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    println(part2(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
