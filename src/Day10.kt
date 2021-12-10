fun main() {
    val scorePart1 = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    val scorePart2 = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun checkIncomplete(
        line: String,
        countRound: Int,
        countSquare: Int,
        countCurly: Int,
        countTag: Int,
        incompleteChar: List<Char>
    ): List<Char> {
        return when {
            countRound > 0 -> checkIncomplete(line, 0, countSquare, countCurly, countTag, incompleteChar + ')')
            countSquare > 0 -> checkIncomplete(line, countRound, 0, countCurly, countTag, incompleteChar + ']')
            countCurly > 0 -> checkIncomplete(line, countRound, countSquare, 0, countTag, incompleteChar + '}')
            countTag > 0 -> checkIncomplete(line, countRound, countSquare, countCurly, 0, incompleteChar + '>')
            line.isEmpty() -> incompleteChar
            else -> {
                val firstChar = line.first()
                val cr = when (firstChar) {
                    '(' -> 1
                    ')' -> -1
                    else -> 0
                }
                val cs = when (firstChar) {
                    '[' -> 1
                    ']' -> -1
                    else -> 0
                }
                val cc = when (firstChar) {
                    '{' -> 1
                    '}' -> -1
                    else -> 0
                }
                val ct = when (firstChar) {
                    '<' -> 1
                    '>' -> -1
                    else -> 0
                }
                checkIncomplete(
                    line.drop(1),
                    countRound + cr,
                    countSquare + cs,
                    countCurly + cc,
                    countTag + ct,
                    incompleteChar
                )
            }
        }
    }


    fun checkString(input: String, bracketStack: List<Char>): Int {
        if (input.isEmpty()) return 0
        return when (input.first()) {
            in ">)}])" -> checkString(input.drop(1), bracketStack + input.first())
            '<' -> return if (bracketStack.isNotEmpty() && bracketStack.last() != '>') scorePart1[bracketStack.last()]!! else checkString(
                input.drop(1),
                bracketStack.dropLast(1)
            )
            '(' -> return if (bracketStack.isNotEmpty() && bracketStack.last() != ')') scorePart1[bracketStack.last()]!! else checkString(
                input.drop(1),
                bracketStack.dropLast(1)
            )
            '{' -> return if (bracketStack.isNotEmpty() && bracketStack.last() != '}') scorePart1[bracketStack.last()]!! else checkString(
                input.drop(1),
                bracketStack.dropLast(1)
            )
            else -> return if (bracketStack.isNotEmpty() && bracketStack.last() != ']') scorePart1[bracketStack.last()]!! else checkString(
                input.drop(1),
                bracketStack.dropLast(1)
            )
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { checkString(it.reversed(), emptyList()) }.sum()
    }

    fun part2(input: List<String>): Long {
        val results = input.filter { checkString(it.reversed(), emptyList()) == 0 }
            .map { checkIncomplete(it.reversed(), 0, 0, 0, 0, emptyList()) }
        val mappedResult = results.map { it.fold(0L) { acc, char -> acc * 5 + scorePart2[char]!! } }
        return mappedResult.sorted()[mappedResult.size / 2]
    }

    val input = readInput("day10")
    println(part1(input))
    println(part2(input))
}