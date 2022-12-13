fun main() {

    fun Char.getHeight(): Int =
        when (this) {
            'S' -> 'a'.code
            'E' -> 'z'.code
            else -> this.code
        }

    fun Point.toValid(input: Map<Point, Char>): List<Point> {
        val data = arrayOf((-1 to 0), (1 to 0), (0 to -1), (0 to 1))
            .map { (dx, dy) -> Point(x + dx, y + dy) }

        return data.filter { it in input && input[it]!!.getHeight() - input[this]!!.getHeight() >= -1 }
    }

    fun move(input: Map<Point, Char>, currentPosition: Char): Int {
        val start = input.entries.first { it.value == 'E' }.key
        val distances = input.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        distances[start] = 0

        val toVisit = mutableListOf(start)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            current.toValid(input)
                .forEach { neighbour ->
                    val newDistance = distances[current]!! + 1

                    if (input[neighbour] == currentPosition) return newDistance

                    if (newDistance < distances[neighbour]!!) {
                        distances[neighbour] = newDistance
                        toVisit.add(neighbour)
                    }
                }
        }
        return -1
    }

    fun part1(input: Map<Point, Char>): Int = move(input, 'S')

    fun part2(input: Map<Point, Char>): Int = move(input, 'a')

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test").toPoints()
    check(part1(testInput) == 31)
    println(part2(testInput))

    val input = readInput("Day12").toPoints()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toPoints(): Map<Point, Char> = this
    .flatMapIndexed { y: Int, row: String ->
        row.mapIndexed { x, c ->
            Point(x, y) to c
        }
    }
    .toMap()



