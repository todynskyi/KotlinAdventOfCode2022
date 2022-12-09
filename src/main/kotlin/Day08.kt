fun main() {

    fun part1(input: List<String>): Int {
        val data: List<List<Int>> = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }
        }

        fun top(rowIndex: Int, colIndex: Int): Boolean {
            return (0 until rowIndex).map { data[it][colIndex] }.all { data[rowIndex][colIndex] > it }
        }

        fun bottom(rowIndex: Int, colIndex: Int): Boolean {
            return (rowIndex + 1 until data.size).map { data[it][colIndex] }.all { data[rowIndex][colIndex] > it }
        }

        fun left(rowIndex: Int, colIndex: Int): Boolean {
            return (0 until colIndex).map { data[rowIndex][it] }.all { data[rowIndex][colIndex] > it }
        }

        fun right(rowIndex: Int, colIndex: Int, size: Int): Boolean {
            return (colIndex + 1 until size).map { data[rowIndex][it] }.all { data[rowIndex][colIndex] > it }
        }

        val convered = data.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, value ->
                if (rowIndex == 0 || rowIndex == data.size - 1 || colIndex == 0 || colIndex == row.size - 1) {
                    1
                } else {
                    if (
                        top(rowIndex, colIndex) ||
                        bottom(rowIndex, colIndex) ||
                        left(rowIndex, colIndex) ||
                        right(rowIndex, colIndex, row.size)
                    ) 1 else 0
                }
            }
        }

        return convered.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val data: List<List<Int>> = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }
        }

        fun top(rowIndex: Int, colIndex: Int): Int {
            val neighborhoods = (rowIndex - 1..0).map { data[it][colIndex] }
            val matched = neighborhoods.takeWhile { data[rowIndex][colIndex] > it }.count()
            return if (neighborhoods.size > 1 && matched > 0 ) matched + 1 else matched
        }

        fun bottom(rowIndex: Int, colIndex: Int): Int {
            val neighborhoods = (rowIndex + 1 until data.size).map { data[it][colIndex] }
            val matched = neighborhoods.takeWhile { data[rowIndex][colIndex] > it }.count()
            return if (neighborhoods.size > 1 && matched > 0 ) matched + 1 else matched
        }

        fun left(rowIndex: Int, colIndex: Int): Int {
            val neighborhoods =  (colIndex - 1..0).map { data[rowIndex][it] }
            val matched = neighborhoods.takeWhile { data[rowIndex][colIndex] > it }.count()
            return if (neighborhoods.size > 1 && matched > 0 ) matched + 1 else matched
        }

        fun right(rowIndex: Int, colIndex: Int, size: Int): Int {
            val neighborhoods =  (colIndex + 1 until size).map { data[rowIndex][it] }
            val matched = neighborhoods.takeWhile { data[rowIndex][colIndex] > it }.count()
            return if (neighborhoods.size > 1 && matched > 0 ) matched + 1 else matched
        }

        val convered = data.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, value ->
                if (rowIndex == 0 || rowIndex == data.size - 1 || colIndex == 0 || colIndex == row.size - 1) {
                    0
                } else {
                    Math.max(1, top(rowIndex, colIndex)) *
                            Math.max(1, bottom(rowIndex, colIndex)) *
                            Math.max(1, left(rowIndex, colIndex)) *
                            Math.max(1, right(rowIndex, colIndex, row.size))
                }
            }
        }

        return (convered.maxOf { it.max() })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    println(part2(testInput))

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
