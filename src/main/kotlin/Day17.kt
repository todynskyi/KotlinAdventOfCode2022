fun main() {

    val up = Point(0, -1)
    val left = Point(-1, 0)
    val right = Point(1, 0)

    val rocks = listOf(
        (0..3).map { i -> Point(i, 0) }.toSet(),
        setOf(Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2), Point(1, 0)),
        setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2)),
        (0..3).map { i -> Point(0, i) }.toSet(),
        setOf(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0))
    )

    fun convertToCacheKey(rocks: Set<Point>): Set<Point> {
        val maxY = rocks.map { it.y }.max()
        return rocks.filter { maxY - it.y <= 30 }.map { Point(it.x, maxY - it.y) }.toHashSet()
    }

    fun part1(input: String): Long {
        val rocksInCave = mutableSetOf<Point>()
        for (i in 0..6) {
            rocksInCave.add(Point(i, -1))
        }

        val jetPattern = input.trim().toCharArray().map { it == '>' }

        var jetCounter = 0
        for (rockCount in 0..2021) {
            var curRock: Set<Point> = rocks[rockCount % 5]
            val maxY = rocksInCave.map { it.y }.max()

            curRock = curRock.map { it.sum(Point(2, maxY + 4)) }.toSet()
            movement@ while (true) {
                if (jetPattern[jetCounter % jetPattern.size]) {
                    val highestX = curRock.map { it.x }.max()
                    val tentativeRight = curRock.map { it.sum(right) }.toSet()
                    if (highestX < 6 && !tentativeRight.any { it in rocksInCave }) {
                        curRock = tentativeRight
                    }
                } else {
                    val lowestX = curRock.map { it.x }.min()
                    val tentativeLeft = curRock.map { it.sum(left) }.toSet()
                    if (lowestX > 0 && !tentativeLeft.any { it in rocksInCave }) curRock = tentativeLeft
                }
                jetCounter++

                for (c in curRock) {
                    if (rocksInCave.contains(c.sum(up))) {
                        rocksInCave.addAll(curRock)
                        break@movement
                    }
                }

                curRock = curRock.map { it.sum(up) }.toSet()
            }
        }

        return ((rocksInCave.maxOfOrNull { it.y } ?: 0) + 1).toLong()
    }


    fun part2(input: String): Long {
        val target = 1000000000000L

        val rocksInCave: MutableSet<Point> = mutableSetOf()
        for (i in 0..6) {
            rocksInCave.add(Point(i, -1))
        }

        val jetPattern = input.trim().toCharArray().map { it == '>' }
        var jetCounter = 0

        val cache: MutableMap<Set<Point>, Point> = mutableMapOf()

        var cycleFound = false
        var heightFromCycleRepeat: Long = 0

        var rockCount: Long = 0
        while (rockCount < target) {
            var curRock: Set<Point> = rocks[(rockCount % 5).toInt()]
            val maxY = rocksInCave.map { it.y }.max()

            curRock = curRock.map { it.sum(Point(2, maxY + 4)) }.toSet()
            movement@ while (true) {
                if (jetPattern[jetCounter % jetPattern.size]) {
                    val highestX = curRock.map { it.x }.max()
                    val tentativeRight = curRock.map { it.sum(right) }.toSet()
                    if (highestX < 6 && !tentativeRight.any { it in rocksInCave }) {
                        curRock = tentativeRight
                    }
                } else {
                    val lowestX = curRock.map { it.x }.min()
                    val tentativeLeft = curRock.map { it.sum(left) }.toSet()
                    if (lowestX > 0 && !tentativeLeft.any { it in rocksInCave }) curRock = tentativeLeft
                }
                jetCounter++

                for (c in curRock) {
                    if (rocksInCave.contains(c.sum(up))) {
                        rocksInCave.addAll(curRock)
                        val curHeight = rocksInCave.map { it.y }.max()
                        val cacheKey: Set<Point> = convertToCacheKey(rocksInCave)
                        if (!cycleFound && cache.containsKey(cacheKey)) {
                            val info: Point = cache[cacheKey]!!
                            val oldTime: Int = info.x
                            val oldHeight: Int = info.y
                            val cycleLength = (rockCount - oldTime).toInt()
                            val cycleHeightChange = curHeight - oldHeight
                            val numCycles = (target - rockCount) / cycleLength
                            heightFromCycleRepeat = cycleHeightChange * numCycles
                            rockCount += numCycles * cycleLength
                            cycleFound = true
                        } else {
                            val info = Point(rockCount.toInt(), curHeight)
                            cache[cacheKey] = info
                        }
                        break@movement
                    }
                }

                curRock = curRock.map { it.sum(up) }.toSet()
            }
            rockCount++
        }

        return (rocksInCave.map { it.y }.max() + heightFromCycleRepeat + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test").first()
    check(part1(testInput) == 3068L)
    println(part2(testInput))

    val input = readInput("Day17").first()
    println(part1(input))
    println(part2(input))
}

fun Point.sum(o: Point) = Point(x + o.x, y + o.y)


