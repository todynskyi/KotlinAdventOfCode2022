import kotlin.math.abs
import kotlin.math.sign

fun main() {

    fun part1(input: Set<Point>): Int {
        val units = mutableSetOf<Point>()
        val start = Point(500, 0)
        val last = input.maxOf { it.y }

        var current = start
        while (current.y != last) {
            current = current.move(input, units) ?: start.also { units.add(current) }
        }

        return units.size
    }

    fun part2(input: Set<Point>): Int {
        val units = mutableSetOf<Point>()
        val floor = input.maxOf { it.y } + 2
        val start = Point(500, 0)

        var current = start
        while (start !in units) {
            val next = current.move(input, units)
            current = when (next == null || next.y == floor) {
                true -> start.also { units.add(current) }
                else -> next
            }
        }

        return units.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test").toRocks()
    check(part1(testInput) == 24)
    println(part2(testInput))

    val input = readInput("Day14").toRocks()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toRocks(): Set<Point> = this.flatMap { line ->
    line.split(" -> ")
        .map { it.split(",") }
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }
        .zipWithNext()
        .flatMap { (first, second) -> first.to(second) }
}.toSet()

private fun Point.move(rocks: Set<Point>, exclusion: Set<Point>) = listOf(
    Point(x, y + 1),
    Point(x - 1, y + 1),
    Point(x + 1, y + 1)
).firstOrNull { it !in rocks && it !in exclusion }

private fun Point.to(other: Point): List<Point> {
    val dx = (other.x - x).sign
    val dy = (other.y - y).sign

    val steps = maxOf(abs(x - other.x), abs(y - other.y))

    return (1..steps).scan(this) { last, _ -> Point(last.x + dx, last.y + dy) }
}
