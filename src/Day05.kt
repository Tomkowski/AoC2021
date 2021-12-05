import java.lang.Integer.max
import java.lang.Integer.min

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
data class Coordinates(val x: Int, val y: Int)

fun main() {
    fun loadLines(input: List<String>, filterBy: (line: Line) -> Boolean = { true }): List<Line> {
        val firstCords = input.map { it.split("->")[0] }
        val secondCords = input.map { it.split("->")[1] }

        val lines = firstCords.zip(secondCords).map {
            val (x1, y1) = it.first.trim().split(",").map { number -> number.toInt() }
            val (x2, y2) = it.second.trim().split(",").map { number -> number.toInt() }
            Line(x1, y1, x2, y2)
        }
        return lines.filter(filterBy)
    }

    fun solvePipes(lines: List<Line>): Int {
        fun fillOneLine(start: Int, end: Int, constantAxis: Int, pipeMap: MutableMap<Coordinates, Int>, coordinateGenerator: (Int, Int) -> Coordinates) {
            val bigger = max(start, end)
            val smaller = min(start, end)
            (smaller..bigger).forEach {
                val cords = coordinateGenerator(it, constantAxis)
                val currentValue = pipeMap[cords] ?: 0
                pipeMap[cords] = currentValue + 1
            }
        }

        val pipeMap = mutableMapOf<Coordinates, Int>()
        lines.forEach { line ->
            when {
                line.y1 == line.y2 -> {
                    fillOneLine(line.x1, line.x2, line.y1, pipeMap) {step, axis -> Coordinates(step, axis)}
                }
                line.x1 == line.x2 -> {
                    fillOneLine(line.y1, line.y2, line.x1, pipeMap) {step, axis -> Coordinates(axis, step)}
                }
                else -> {
                    val xSet = if (line.x1 < line.x2) (line.x1..line.x2) else (line.x1 downTo line.x2)
                    val ySet = if (line.y1 < line.y2) (line.y1..line.y2) else (line.y1 downTo line.y2)

                    xSet.zip(ySet).forEach {
                        val cords = Coordinates(it.first, it.second)
                        val currentValue = pipeMap[cords] ?: 0
                        pipeMap[cords] = currentValue + 1
                    }
                }
            }
        }
        return pipeMap.values.count { it > 1 }
    }

    fun part1(input: List<String>): Int {
        val lines = loadLines(input) { line -> line.x1 == line.x2 || line.y1 == line.y2 }
        return solvePipes(lines)
    }

    fun part2(input: List<String>): Int {
        val lines = loadLines(input)
        return solvePipes(lines)
    }

    val input = readInput("day05")
    println(part1(input))
    println(part2(input))
}