fun main() {

    fun mostCommon(input: List<String>, predicate: (List<String>, Int, Int) -> Char): String {
        fun mc(input: List<String>, i: Int): String {
            val inputSize = input.size
            if (inputSize == 1) return input[0]

            val commonBit = predicate(input, inputSize, i)
            return mc(input.filter { it[i] == commonBit }, i + 1)
        }
        return mc(input, 0)
    }

    fun part1(input: List<String>): Int {
        val inputSize = input.size
        val binaryLength = input[0].length

        val gamma =
            (0 until binaryLength).mapIndexed { i, _ -> if (input.count { (it[i] == '1') } > inputSize / 2) '1' else '0' }
                .joinToString("")
        val epsilon =
            (0 until binaryLength).mapIndexed { i, _ -> if (input.count { (it[i] == '1') } > inputSize / 2) '0' else '1' }
                .joinToString("")

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygen = mostCommon(input) { list, inputSize, i ->
            if (list.count { it[i] == '1' } >= inputSize / 2.0) '1' else '0'
        }
        val carbon = mostCommon(input) { list, inputSize, i ->
            if (list.count { it[i] == '0' } <= inputSize / 2.0) '0' else '1'
        }
        return oxygen.toInt(2) * carbon.toInt(2)
    }

    val input = readInput("day03")
    println(part1(input))
    println(part2(input))
}