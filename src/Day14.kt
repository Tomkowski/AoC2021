val scoreMap = mutableMapOf<String, ULong>()
val previousScoreMap = mutableMapOf<String, ULong>()
val codeOccurrences = mutableMapOf<String, ULong>()

fun sumRules(formulaMap: Map<String, String>) {
    val keySet = codeOccurrences.keys.toSet()
    keySet.forEach {
        val firstPair = it[0] + formulaMap[it]!!
        val secondPair = formulaMap[it]!! + it[1]

        previousScoreMap[firstPair] =
            previousScoreMap[firstPair]?.let { score -> score + codeOccurrences[it]!! } ?: codeOccurrences[it]!!

        previousScoreMap[secondPair] =
            previousScoreMap[secondPair]?.let { score -> score + codeOccurrences[it]!! } ?: codeOccurrences[it]!!

        scoreMap[formulaMap[it]!!] = scoreMap[formulaMap[it]!!]?.let { score -> score + codeOccurrences[it]!! } ?: 1u
        codeOccurrences[it] = 0u
    }
    keySet.forEach {
        val firstPair = it[0] + formulaMap[it]!!
        val secondPair = formulaMap[it]!! + it[1]
        codeOccurrences[firstPair] = previousScoreMap[firstPair]!!
        codeOccurrences[secondPair] = previousScoreMap[secondPair]!!
    }
    previousScoreMap.clear()
}

fun main() {
    val input = readInput("day14")
    val initialString = input[0]
    initialString.forEach { value -> scoreMap["$value"] = scoreMap["$value"]?.let { score -> score + 1u } ?: 1u }
    val formulas = input.takeLast(input.size - 2)
    val formulaMap = formulas.map {
        val (key, value) = it.split(" -> ")
        key to value
    }.toMap()

    initialString.windowed(2)
        .forEach { codeOccurrences[it] = codeOccurrences[it]?.let { score -> score + 1u } ?: 1u }

    fun solve(depth: Int): ULong{
        (0 until depth).forEach { _ ->
            sumRules(formulaMap)
        }
        val smallestSet = scoreMap.minByOrNull { it.value }?.value ?: 0u
        val biggestSet = scoreMap.maxByOrNull { it.value }?.value ?: 0u
        return biggestSet - smallestSet
    }

    println(solve(10))
    //Global state doesn't reset already calculated result. 40 - 10 (part1) = 30
    println(solve(30))
}