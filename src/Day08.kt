fun main() {
    fun part1(input: List<String>): Int {
        val riddle = input.map { it.split(" | ")[0] }
        val output = input.map { it.split(" | ")[1] }

        return output.map { row ->
            row.split(" ").count { segment ->
                segment.length == 2 || segment.length == 3 || segment.length == 4 || segment.length == 7
            }
        }.sum()
    }

    val digitDictionary = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9
    )

    /**
     * inter -> intersection
     * sub -> subtract
     *
     * 7 sub 1 == a
     * (all size of 6) where some digit appears 2 times -> cde. 4 inter cde = e
     * 8 sub 4 sub a sub e = g
     * (all size of 5) - aeg == 2 -> 2 intersect 1 = c
     * size 5 - aeg == 2 -> 2 sub 1 = f
     * (8 sub aceg) intersect 5 = d
     * (8 sub acdefg) = b
     */
    fun solveRiddle(riddleInput: String, output: String): Int {
        val brokenDigits = mutableMapOf<Char, Char>()
        val riddle = riddleInput.split(" ").map { it.toCharArray().toList() }.groupBy { it.size }

        val a = riddle[3]!!.first().subtract(riddle[2]!!.first()).first()
        brokenDigits[a] = 'a'

        val e = (riddle[6]!!.joinToString("").groupBy { it }
            .filter { it.value.size == 2 }.values.map { list -> list[0] }).subtract(riddle[4]!!.first()).first()
        brokenDigits[e] = 'e'

        val g = riddle[7]!!.first().subtract(listOf(a, e)).subtract(riddle[4]!!.first()).first()
        brokenDigits[g] = 'g'

        val number2 = riddle[5]!!.find { (it subtract listOf(a, e, g)).size == 2 }

        val c = number2!!.intersect(riddle[2]!!.first()).first()
        brokenDigits[c] = 'c'
        val f = riddle[2]!!.first().subtract(listOf(c)).first()
        brokenDigits[f] = 'f'

        val d = (riddle[7]!!.first().subtract(listOf(a, c, g, e))).intersect(number2).first()
        brokenDigits[d] = 'd'

        val b = riddle[7]!!.first().subtract(listOf(a, d, c, e, f, g)).first()
        brokenDigits[b] = 'b'

        val mapped = output.split(" ")
            .map { number -> number.map { digit -> brokenDigits[digit] }.sortedBy { it }.joinToString("") }
        return mapped.fold(0) { acc, number ->
            10 * acc + digitDictionary[number]!!
        }
    }

    fun part2(input: List<String>): Int {
        val riddle = input.map { it.split(" | ")[0] }
        val output = input.map { it.split(" | ")[1] }

        return riddle.zip(output).fold(0) { acc, pair -> acc + solveRiddle(pair.first, pair.second) }
    }

    val input = readInput("day08")
    println(part1(input))
    println(part2(input))
}