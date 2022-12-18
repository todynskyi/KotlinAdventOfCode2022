fun main() {

    fun part1(input: List<Coordinate>): Int = input.sumOf { it.neighbors().count { p -> p !in input } }

    fun part2(input: List<Coordinate>): Int {

        val minX = input.minBy { it.x }.x
        val minY = input.minBy { it.y }.y
        val minZ = input.minBy { it.z }.z

        val maxX = input.maxBy { it.x }.x
        val maxY = input.maxBy { it.y }.y
        val maxZ = input.maxBy { it.z }.z

        val area = mutableSetOf<Coordinate>()
        val start = Coordinate(minX - 1, minY - 1, minZ - 1)
        area.add(start)

        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            for (neighbor in queue.removeLast().neighbors()) {
                if (neighbor.x in minX - 1..maxX + 1 &&
                    neighbor.y in minY - 1..maxY + 1 &&
                    neighbor.z in minZ - 1..maxZ + 1 &&
                    neighbor !in input && area.add(neighbor)
                ) {
                    queue.add(neighbor)
                }
            }

        }
        return input.sumOf { it.neighbors().count { p -> p in area } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test").toCoordinates()
    check(part1(testInput) == 64)
    println(part2(testInput))

    val input = readInput("Day18").toCoordinates()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toCoordinates(): List<Coordinate> = this.map {
    val (x, y, z) = it.split(",")
    Coordinate(x.toInt(), y.toInt(), z.toInt())
}

data class Coordinate(val x: Int, val y: Int, val z: Int) {
    fun neighbors() = listOf(
        copy(x = x - 1),
        copy(x = x + 1),
        copy(y = y - 1),
        copy(y = y + 1),
        copy(z = z - 1),
        copy(z = z + 1),
    )
}