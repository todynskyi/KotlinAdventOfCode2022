fun main() {

    fun cd(root: Folder, current: Folder, name: String): Folder = when (name) {
        "/" -> root
        ".." -> {
            if (current.name == "/") {
                current
            } else {
                val parent = requireNotNull(current.parent)
                parent.copy(child = parent.child.filter { it.folder && it.name != current.name } + parent.child.filter { !it.folder } + current)
            }
        }

        else -> current.child.first { it.folder && it.name == current.name + name }.copy(parent = current)
    }

    fun calculate(root: Folder): Map<String, Long> {
        val sizes = mutableMapOf<String, Long>()
        fun compute(root: Folder): Int {
            return if (root.folder) {
                root.child.fold(0) { acc, folder ->
                    val size = compute(folder)
                    sizes[root.name] = sizes.getOrDefault(root.name, 0) + size
                    acc + size
                }
            } else {
                root.size ?: 0
            }
        }

        compute(root)
        return sizes
    }

    fun parse(input: List<String>): Folder {
        val root = Folder("/", folder = true)
        var current = root

        input.forEach { line ->
            if (line.startsWith("\$ cd ")) {
                current = cd(root, current, line.replace("\$ cd ", ""))
            } else if (line == "\$ ls") {
                //fileSystem.ls()
            } else if (line.startsWith("dir ")) {
                current = current.copy(
                    child = current.child + Folder(
                        name = current.name + line.replace("dir ", ""),
                        folder = true,
                        parent = current
                    )
                )
            } else {
                val parts = line.split(" ")
                current = current.copy(
                    child = current.child + Folder(
                        name = parts.last(),
                        folder = false,
                        size = parts.first().toInt()
                    )
                )
            }
        }
        while (current.name != "/") {
            current = cd(root, current, "..")
        }
        return current
    }

    fun part1(input: List<String>): Long = calculate(parse(input)).values.filter { it < 100000 }.sum()

    fun part2(input: List<String>): Long {
        val sizes = calculate(parse(input))
        val total = 70000000 - (sizes["/"] ?: 0)
        return sizes.values.sorted().first { total + it >= 30000000 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    println(part2(testInput))

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

class FileSystem() {

    private var current: Folder = Folder("/", true)

    fun ls() {
    }

    fun cd(command: String) {
        current = when (command) {
            "/" -> current
            ".." -> requireNotNull(current.parent)
            else -> current.child.first { it.folder && it.name == command }.copy(parent = current.parent)
        }
    }

    fun dir(folderName: String) {
        current = current.copy(child = current.child + Folder(folderName, true, parent = current))
    }

    fun create(fileName: String, size: Int) {
        current = current.copy(child = current.child + Folder(fileName, false, size = size))
    }
}

data class Folder(
    val name: String,
    val folder: Boolean,
    val size: Int? = null,
    val parent: Folder? = null,
    val child: List<Folder> = emptyList()
)
