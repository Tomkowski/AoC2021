import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {

    fun factorial(i: Int): Int = if (i == 0) 0 else i + factorial((i - 1))

    fun matchCrabsToLine(line: Int, crabs: List<Int>, fuelStrategy: (Int, Int) -> Int): Long {
        return crabs.fold(0L) { acc, crab -> acc + fuelStrategy(line, crab) }
    }

    fun part1(input: List<String>): Long {
        val mapped = input[0].split(",").map { it.toInt() }
        val lowerBoundary = mapped.minOrNull() ?: 0
        val upperBoundary = mapped.maxOrNull() ?: 1 shl 20
        val fuelStrategy: (Int, Int) -> Int = { line, crabPosition -> abs(line - crabPosition) }

        return (lowerBoundary..upperBoundary).map { matchCrabsToLine(it, mapped, fuelStrategy) }.minOrNull() ?: 0
    }

    fun part2(input: List<String>): Long {
        val mapped = input[0].split(",").map { it.toInt() }
        val fuelStrategy: (Int, Int) -> Int = { line, crab -> factorial(abs(line - crab)) }
        val averagePosition = mapped.average().roundToInt()

        return (averagePosition - 3..averagePosition + 3).map { matchCrabsToLine(it, mapped, fuelStrategy) }.minOrNull() ?: 0
    }

    val input = readInput("day07")
    println(part1(input))
    println(part2(input))
}