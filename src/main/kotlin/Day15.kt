import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {

    fun isValid(point: Point, sensors: List<Sensor>): Boolean =
        (point.x in 0..MAX_COORDINATES && point.y in 0..MAX_COORDINATES) && sensors.none { (sensor, beacon) ->
            point.distanceTo(sensor) <= sensor.distanceTo(
                beacon
            )
        }

    fun part1(input: List<Sensor>): Int {
        val beacons = input.map { it.beacon }.toSet()
        return input.filter { abs(it.at.y - MAX_Y) < it.distanceToBeacon }
            .fold(mutableSetOf<Int>()) { acc, sensor ->
                val d = sensor.distanceToBeacon - sensor.at.distanceTo(Point(sensor.at.x, MAX_Y))
                (-d..d).asSequence().map { sensor.at.x + it }.filter { Point(it, MAX_Y) !in beacons }
                    .forEach(acc::add)
                acc
            }.size
    }

    fun part2(input: List<Sensor>): Long {
        for (sensor in input.sortedBy { it.distanceToBeacon }) {
            for (x in sensor.distanceToBeacon + 1 downTo 1) for (p in sensor.at.toPoints(
                x, y = sensor.distanceToBeacon + 1 - x
            )) {
                if (isValid(p, input)) {
                    return p.x.toLong() * MAX_COORDINATES + p.y
                }
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test").toSensors()
    check(part1(testInput) == 0)
    println(part2(testInput))

    val input = readInput("Day15").toSensors()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toSensors(): List<Sensor> = this.map { it.split(":") }
    .map { (sensor, beacon) ->
        val (sensorX, sensorY) = sensor.split(",")
        val (beaconX, beaconY) = beacon.split(",")
        Sensor(
            Point(sensorX.substringAfter("x=").toInt(), sensorY.substringAfter("y=").toInt()),
            Point(beaconX.substringAfter("x=").toInt(), beaconY.substringAfter("y=").toInt())
        )
    }

data class Sensor(
    val at: Point,
    val beacon: Point,
    val distanceToBeacon: Int = (at.x - beacon.x).absoluteValue + (at.y - beacon.y).absoluteValue
)

const val MAX_Y = 2000000
const val MAX_COORDINATES = 4000000

fun Point.distanceTo(other: Point): Int =
    (x - other.x).absoluteValue + (y - other.y).absoluteValue

fun Point.toPoints(x: Int, y: Int) =
    sequenceOf(Point(x, y), Point(x, -y), Point(-x, y), Point(-x, -y))
        .map { Point(this.x + it.x, this.y + it.y) }