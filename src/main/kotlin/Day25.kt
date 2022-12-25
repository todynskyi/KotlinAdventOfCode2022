import kotlin.math.pow

fun main() {

    fun snafuToInt(snafu: String): Long = snafu.reversed()
        .withIndex()
        .fold(0L) { acc, indexedValue ->
            val weight = 5.0.pow(indexedValue.index.toDouble()).toLong()
            acc + when (indexedValue.value) {
                '-' -> -weight
                '=' -> -2 * weight
                else -> (indexedValue.value - '0') * weight
            }
        }

    fun intToSnafu(n: Long, prefix: Boolean = false): String = if (n == 0L && prefix) ""
    else if (n == 0L) "0"
    else {
        when (val floorMod = Math.floorMod(n, 5)) {
            3 -> intToSnafu(n / 5 + 1, true) + "="
            4 -> intToSnafu(n / 5 + 1, true) + "-"
            else -> intToSnafu(n / 5, true) + floorMod.toString()
        }
    }

    fun part1(input: List<String>): String = intToSnafu(input.sumOf { snafuToInt(it) })

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == "2=-1=0")

    val input = readInput("Day25")
    println(part1(input))
}
