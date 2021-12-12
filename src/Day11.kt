fun main() {

    data class Cord(val x: Int, val y: Int)

    fun findNeighbours(map: List<CharArray>, cord: Cord): List<Cord> {
        return listOf(
            Cord(cord.x - 1, cord.y),
            Cord(cord.x - 1, cord.y - 1),
            Cord(cord.x - 1, cord.y + 1),
            Cord(cord.x, cord.y - 1),
            Cord(cord.x, cord.y + 1),
            Cord(cord.x + 1, cord.y),
            Cord(cord.x + 1, cord.y - 1),
            Cord(cord.x + 1, cord.y + 1),
        ).filter { map[it.x][it.y] in '1'..'9' }
    }

    lateinit var uglyMap: MutableList<CharArray>

    fun spreadLight(cord: Cord): MutableList<CharArray> {
        if (uglyMap[cord.x][cord.y] == '9' + 1) {
            uglyMap[cord.x][cord.y]++ // this line cost me 2 hours
            val neighbours = findNeighbours(uglyMap, Cord(cord.x, cord.y))
            neighbours.forEach { neighbour ->
                uglyMap[neighbour.x][neighbour.y]++
            }
            neighbours.forEach { neighbour ->
                spreadLight(neighbour)
            }
        }

        return uglyMap
    }

    fun simulateLightning(days: Int): Int {
        fun sl(days: Int, result: Int): Int {
            if (days < 1) return result
            for (i in 1 until uglyMap.first().size - 1)
                for (j in 1 until uglyMap.size - 1) {
                    uglyMap[i][j]++
                }

            for (i in 1 until uglyMap.first().size - 1)
                for (j in 1 until uglyMap.size - 1) {
                    spreadLight(Cord(i, j))
                }
            val triggeredOcto = uglyMap.sumOf { row -> row.count { it > '9' && it != 'z' } }
            for (i in 1 until uglyMap.first().size - 1)
                for (j in 1 until uglyMap.size - 1) {
                    if (uglyMap[i][j] > '9') {
                        uglyMap[i][j] = '0'
                    }
                }
            return sl(days - 1, result + triggeredOcto)
        }

        return sl(days, 0)
    }

    fun part1(input: List<String>): Int {
        val horizontalLength = input.first().length
        val mapped = input.map { "z${it}z" }
        val boundaryRow = listOf("z".repeat(horizontalLength + 2))

        /**
         * zzzzz
         * zrowz
         * zrowz
         * zzzzz
         */
        uglyMap = (boundaryRow + mapped + boundaryRow).map { row -> row.toCharArray() }.toMutableList()


        return simulateLightning(
            100
        )
    }

    fun part2(input: List<String>): Int {
        val horizontalLength = input.first().length
        val mapped = input.map { "z${it}z" }
        val boundaryRow = listOf("z".repeat(horizontalLength + 2))

        /**
         * zzzzz
         * zrowz
         * zrowz
         * zzzzz
         */
        uglyMap = (boundaryRow + mapped + boundaryRow).map { row -> row.toCharArray() }.toMutableList()

        var ans = 1
        while (simulateLightning(1) != input.size * input.first().length) {
            ans++
        }
        return ans
    }

    val input = readInput("day11")
    println(part1(input))
    println(part2(input))
}