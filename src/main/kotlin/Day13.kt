fun main() {

    fun part1(input: List<Packet>): Int = input.chunked(2)
        .mapIndexed { index, packets -> if (packets.first() < packets.last()) index + 1 else 0 }
        .sum()

    fun part2(input: List<Packet>): Int {
        val packet1 = ListPacket(listOf(ListPacket(listOf(ListPacket(listOf(IntPacket(2)))))))
        val packet2 = ListPacket(listOf(ListPacket(listOf(ListPacket(listOf(IntPacket(6)))))))
        val packets = (input + packet1 + packet2).sorted()
        return (packets.indexOf(packet1) + 1) * (packets.indexOf(packet2) + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test").toPackets()
    check(part1(testInput) == 13)
    println(part2(testInput))

    val input = readInput("Day13").toPackets()
    println(part1(input))
    println(part2(input))
}

fun List<String>.toPackets(): List<Packet> = this.filter { it.isNotBlank() }
    .map { line ->
        fun read(iterator: Iterator<String>): Packet {
            val packets = mutableListOf<Packet>()
            while (iterator.hasNext()) {
                when (val c = iterator.next()) {
                    "]" -> return ListPacket(packets)
                    "[" -> packets.add(read(iterator))
                    else -> packets.add(IntPacket(c.toInt()))
                }
            }
            return ListPacket(packets)
        }

        read(
            line.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                .filter { it.isNotBlank() }
                .filter { it != "," }
                .iterator())
    }

sealed class Packet : Comparable<Packet>

class IntPacket(val value: Int) : Packet() {
    override fun compareTo(other: Packet): Int = when (other) {
        is IntPacket -> value.compareTo(other.value)
        is ListPacket -> ListPacket(listOf(this)).compareTo(other)
    }
}

class ListPacket(val packets: List<Packet> = listOf()) : Packet() {
    override fun compareTo(other: Packet): Int = when (other) {
        is IntPacket -> compareTo(ListPacket(listOf(other)))
        is ListPacket -> packets.zip(other.packets)
            .map { it.first.compareTo(it.second) }
            .firstOrNull { it != 0 } ?: packets.size.compareTo(other.packets.size)
    }
}
