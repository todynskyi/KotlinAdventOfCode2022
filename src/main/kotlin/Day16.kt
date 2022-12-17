import kotlin.math.max

fun main() {

    fun floydWarshall(
        shortestPaths: Map<String, MutableMap<String, Int>>,
        valves: Map<String, Valve>
    ): Map<String, Map<String, Int>> {
        for (k in shortestPaths.keys) {
            for (i in shortestPaths.keys) {
                for (j in shortestPaths.keys) {
                    val ik = shortestPaths[i]?.get(k) ?: 9999
                    val kj = shortestPaths[k]?.get(j) ?: 9999
                    val ij = shortestPaths[i]?.get(j) ?: 9999
                    if (ik + kj < ij) {
                        shortestPaths[i]?.set(j, ik + kj)
                    }
                }
            }
        }
        shortestPaths.values.forEach {
            it.keys.map { key -> if (valves[key]?.flowRate == 0) key else "" }
                .forEach { toRemove -> if (toRemove != "") it.remove(toRemove) }
        }
        return shortestPaths
    }

    fun calculate(input: List<Valve>, totalTime: Int, optimal: Boolean): Int {
        var score = 0

        val valves = input.associateBy { it.id }

        val shortestPaths =
            floydWarshall(
                input.associate { it.id to it.neighbors.associateWith { 1 }.toMutableMap() }.toMutableMap(),
                valves
            )

        fun calculate(currScore: Int, currentValve: String, visited: Set<String>, time: Int, optimal: Boolean) {
            score = max(score, currScore)
            for ((valve, dist) in shortestPaths[currentValve]!!) {
                if (!visited.contains(valve) && time + dist + 1 < totalTime) {
                    calculate(
                        currScore + (totalTime - time - dist - 1) * valves[valve]?.flowRate!!,
                        valve,
                        visited.union(listOf(valve)),
                        time + dist + 1,
                        optimal
                    )
                }
            }
            if (optimal) {
                calculate(currScore, "AA", visited, 0, false)
            }
        }
        calculate(0, "AA", emptySet(), 0, optimal)
        return score
    }

    fun part1(input: List<Valve>): Int = calculate(input, PART1_TIME, false)

    fun part2(input: List<Valve>): Int = calculate(input, PART2_TIME, true)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test").toValves()
    check(part1(testInput) == 1651)
    println(part2(testInput))

    val input = readInput("Day16").toValves()
    println(part1(input))
    println(part2(input))
}

const val PART1_TIME = 30
const val PART2_TIME = 26

fun List<String>.toValves(): List<Valve> = this.map {
    val (left, right) = it.split("; ")
    val (id, flowRate) = left.split(" has flow rate=")

    Valve(
        id.substringAfter("Valve "),
        flowRate.toInt(),
        right.replace("tunnels lead to valves ", "")
            .replace("tunnel leads to valve ", "")
            .split(", ").map { valve -> valve.trim() }
    )
}

data class Valve(val id: String, val flowRate: Int, val neighbors: List<String>)
