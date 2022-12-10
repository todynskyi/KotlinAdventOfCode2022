import kotlin.math.abs

fun main() {

    fun count(moves: List<Move>, length: Int): Int {
        val visited = mutableSetOf<Point>()
        var head = Point()
        val tail = mutableListOf<Point>()


        (0 until length).forEach {
            tail.add(Point())
        }

        moves.forEach { move ->
            (0 until move.distance).forEach {
                head = head.move(move)
                var neighbour = head
                for (j in 0 until length) {
                    tail[j] = tail[j].follow(neighbour)
                    neighbour = tail[j]
                }
                visited.add(tail[tail.size - 1])
            }
        }

        return visited.size
    }

    fun part1(input: List<Move>): Int = count(input, 1)

    fun part2(input: List<Move>): Int = count(input, 9)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
        .map { Move(it[0].toString(), it.split(" ").last().toInt()) }
    check(part1(testInput) == 13)
    println(part2(testInput))

    val input = readInput("Day09")
        .map { Move(it[0].toString(), it.split(" ").last().toInt()) }
    println(part1(input))
    println(part2(input))
}

data class Move(val direction: String, val distance: Int)

data class Point(val x: Int = 0, val y: Int = 0) {
    fun move(move: Move): Point {
        return when (move.direction) {
            "U" -> Point(x, y + 1)
            "D" -> Point(x, y - 1)
            "L" -> Point(x - 1, y)
            "R" -> Point(x + 1, y)
            else -> Point(x + 1, y)
        }
    }

    fun follow(head: Point): Point {
        val shouldMove = abs(x - head.x) > 1 || abs(y - head.y) > 1
        if (!shouldMove) {
            return this
        }

        var x1 = x
        var y1 = y
        if (x1 > head.x) {
            x1--
        } else if (x1 < head.x) {
            x1++
        }
        if (y1 > head.y) {
            y1--
        } else if (y1 < head.y) {
            y1++
        }
        return Point(x1, y1)
    }
}