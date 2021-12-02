fun main() {
    fun part1(input: List<String>): Int {
        val mapped =  input.map { it.toInt() }
        return mapped.filterIndexed { index, _ -> if (index > 0) {mapped[index - 1] < mapped[index]} else false }.size
    }

    fun part2(input: List<String>): Int {
        val mapped = input.map { it.toInt() }
        return mapped.filterIndexed { index, _ -> if (index > 2) mapped[index - 3] < mapped[index] else false }.size
    }

    val input = readInput("day01")
    println(part1(input))
    println(part2(input))
}