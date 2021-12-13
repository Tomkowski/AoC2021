import kotlin.math.abs

enum class FoldStrategy {
    x, y
}

data class Cord(val x: Int, val y: Int)

fun main() {

    fun applyFold(input: List<Cord>, instruction: String): List<Cord> {
        val (foldStrategy, foldPower) = instruction.split("fold along ")[1].split("=")
        return when (FoldStrategy.valueOf(foldStrategy)) {
            FoldStrategy.x -> {
                input.map {
                    Cord(
                        (if (foldPower > it.x) it.x + abs(it.x - foldPower) * 2 else it.x) - foldPower - 1,
                        it.y
                    )
                }
            }

            FoldStrategy.y -> {
                input.map {
                    Cord(
                        it.x,
                        if (foldPower < it.y) it.y - abs(it.y - foldPower) * 2 else it.y
                    )
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val emptyLineIndex = input.indexOf("")
        val dots = input.take(emptyLineIndex).map {
            val (x, y) = it.split(",")
            Cord(x.toInt(), y.toInt())
        }
        val instructions = input.takeLast(input.size - emptyLineIndex - 1)

        return applyFold(dots, instructions[0]).toSet().size
    }


    fun part2(input: List<String>) {
        val dots = input.take(input.indexOf("")).map {
            val (x, y) = it.split(",")
            Cord(x.toInt(), y.toInt())
        }
        val instructions = input.takeLast(input.size - input.indexOf("") - 1)
        val mappedDots = instructions.fold(dots) { acc, instruction -> applyFold(acc, instruction) }.toSet()
        for (i in (mappedDots.minByOrNull { it.y }?.y ?: 0)..(mappedDots.maxByOrNull { it.y }?.y ?: 1000)) {
            for (j in (mappedDots.maxByOrNull { it.x }?.x ?: 1000) downTo (mappedDots.minByOrNull { it.x }?.x ?: 0)) {
                print(if (mappedDots.contains(Cord(j, i))) "#" else ".")
            }
            println()
        }
    }

    val input = readInput("day13")
    println(part1(input))
    println(part2(input))
}