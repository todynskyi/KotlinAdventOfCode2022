fun main() {

    fun simulate(original: List<String>, width: Int, height: Int, points: Set<Point>, end: Point, time: Int): Int =
        if (points.contains(end)) time else {
            val next = points.flatMap { it.neighbours() }.filter { it.valid(original, width, height, time + 1) }.toSet()
            simulate(original, width, height, next, end, time + 1)
        }

    fun part1(input: List<String>): Int {
        val width = input.first().length - 2
        val height = input.size - 2
        return simulate(input, width, height, setOf(Point(1, 0)), Point(width, height + 1), 0)
    }

    fun part2(input: List<String>): Int {
        val width = input.first().length - 2
        val height = input.size - 2

        val start = Point(1, 0)
        val end = Point(width, height + 1)

        val firstTrip = simulate(input, width, height, setOf(start), end, 0)
        val backTrip = simulate(input, width, height, setOf(end), start, firstTrip)
        return simulate(input, width, height, setOf(start), end, backTrip)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput) == 18)
    println(part2(testInput))

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}

val moves = listOf(Point(0, 0), Point(1, 0), Point(-1, 0), Point(0, 1), Point(0, -1))

val mod: (Int, Int) -> Int = { a, m ->
    val remainder = (a - 1) % m
    if (remainder < 0) remainder + m + 1 else remainder + 1
}

fun Point.valid(input: List<String>, width: Int, height: Int, time: Int): Boolean =
    input.indices.contains(y) && input[y][x] != '#'
            && input[y][mod(x + time, width)] != '<'
            && input[y][mod(x - time, width)] != '>'
            && input[mod(y + time, height)][x] != '^'
            && input[mod(y - time, height)][x] != 'v'

fun Point.add(other: Point): Point = Point(x + other.x, y + other.y)
fun Point.neighbours(): List<Point> = moves.map { it.add(this) }