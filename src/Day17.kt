import kotlin.math.abs

fun main() {

    fun shoot(x: Int, y: Int, startX: Int, endX: Int, startY: Int, endY: Int): Boolean {
        tailrec fun validShot(currX: Int, currY: Int, x: Int, y: Int): Boolean {
            if (currX + x.coerceAtLeast(0) in startX..endX && currY + y in startY..endY) return true
            return if (currX > endX || currY < endY) false
            else validShot(currX + x.coerceAtLeast(0), currY + y , x - 1, y - 1)
        }
        return validShot(0, 0, x, y)
    }

    fun part1(input: List<String>): Int {
        val data = input[0].split(": ")[1]
        val (minX, maxX) = data.split(", ")[0].split("x=")[1].split("..").map { it.toInt() }
        val (minY, maxY) = data.split(", ")[1].split("y=")[1].split("..").map { it.toInt() }
        println("$minX $maxX")
        println("$minY $maxY")

        var ans = 0

        (1..10000).forEach { y ->
            val heightPoint = y * (y + 1) / 2
            val lowerBoundary = abs(heightPoint - maxY)
            val upperBoundary = abs(heightPoint - minY)
            (-5..5).map { bias -> (y - bias) * (y - bias + 1) / 2 }.forEach { num ->
                if (num in lowerBoundary..upperBoundary) {
                    ans = y
                }
            }
        }
        return ans * (ans + 1) / 2
    }

    fun part2(input: List<String>): Int {
        val allPairs = mutableListOf<Pair<Int, Int>>()
        val data = input[0].split(": ")[1]
        val (minX, maxX) = data.split(", ")[0].split("x=")[1].split("..").map { it.toInt() }
        val (minY, maxY) = data.split(", ")[1].split("y=")[1].split("..").map { it.toInt() }

        (1..1000).forEach { x ->
            (-500..500).forEach { y ->
                if (shoot(x, y, minX, maxX, minY, maxY)) allPairs.add(x to y)
            }
        }
        return allPairs.size
    }

    val input = readInput("day17")
    println(part1(input))
    println(part2(input))
}