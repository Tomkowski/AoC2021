fun main() {
    data class CaveCord(val value: Char, val i: Int, val j: Int)
    val scoreForCordExists = mutableMapOf<CaveCord, Int>()

    fun isLargestInAdjacents(caveMap: List<String>, i: Int, j: Int): Boolean {
        with(caveMap[i][j]) {
            return this < caveMap[i - 1][j] && this < caveMap[i + 1][j] && this < caveMap[i][j - 1] && this < caveMap[i][j + 1]
        }
    }

    fun findAdjacentMinimum(caveMap: List<String>, i: Int, j: Int, from: Char): CaveCord {
        val visited = caveMap.map { row -> row.map { false }.toMutableList() }
        fun dfs(caveMap: List<String>, i: Int, j: Int, from: Char): CaveCord {
            if (visited[i][j] || caveMap[i][j] == 'z' || caveMap[i][j] >= from) return CaveCord('z', i, j)
            visited[i][j] = true
            with(caveMap[i][j]) {
                return if (this < caveMap[i - 1][j] && this < caveMap[i + 1][j] && this < caveMap[i][j - 1] && this < caveMap[i][j + 1]) CaveCord(caveMap[i][j], i, j)
                else listOf(
                    findAdjacentMinimum(caveMap, i - 1, j, this),
                    findAdjacentMinimum(caveMap, i + 1, j, this),
                    findAdjacentMinimum(caveMap, i, j - 1, this),
                    findAdjacentMinimum(caveMap, i, j + 1, this)
                ).minByOrNull { it.value } ?: CaveCord('z', -1, -1)
            }
        }
        return dfs(caveMap, i, j, from)
    }

    fun findBasin(caveMap: List<String>, i: Int, j: Int): Int {
        val visited = caveMap.map { row -> row.map { false }.toMutableList() }

        fun fb(i: Int, j: Int, flowFrom: Char): Int {
            if (visited[i][j]) return 0
            if (caveMap[i][j] == 'z' || caveMap[i][j] == '9' || caveMap[i][j] <= flowFrom) return 0
            visited[i][j] = true
            return 1 + fb(i + 1, j, caveMap[i][j]) + fb(i - 1, j, caveMap[i][j]) + fb(i, j + 1, caveMap[i][j]) + fb(
                i,
                j - 1,
                caveMap[i][j]
            )
        }

        val basinMinimum = findAdjacentMinimum(caveMap, i, j, '9')
        if (scoreForCordExists[basinMinimum] != null) return -1
        val ans = fb(basinMinimum.i, basinMinimum.j, '.')
        scoreForCordExists[basinMinimum] = ans
        return ans
    }

    fun findLowest(input: List<String>): Int {
        val mapped = input.map { "z${it}z" }
        val horizontalLength = input.first().length + 1
        val caveMap = listOf("z".repeat(horizontalLength + 2)) + mapped + listOf("z".repeat(horizontalLength + 2))
        val verticalLength = input.size + 1

        var ans = 0
        for (i in 1 until verticalLength) {
            for (j in 1 until horizontalLength) {
                if (isLargestInAdjacents(caveMap, i, j)) ans += 1 + caveMap[i][j].digitToInt()
            }
        }
        return ans
    }

    fun part1(input: List<String>): Int {
        return findLowest(input)
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
        val caveMap = boundaryRow + mapped + boundaryRow
        val results = caveMap.mapIndexed { indexI, row -> row.mapIndexed { indexJ, _ -> findBasin(caveMap, indexI, indexJ) } }.flatten()
        return results.sortedDescending().take(3).fold(1) { acc, basin -> acc * basin }
    }

    val input = readInput("day09")
    println(part1(input))
    println(part2(input))
}