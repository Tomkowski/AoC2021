import java.util.*

fun main() {
    data class CaveCord(val x: Int, val y: Int)

    fun traverseGraph(caveMap: List<MutableList<Int>>, caveValues: List<MutableList<Int>>) {
        val queue = PriorityQueue(compareBy<CaveCord> { caveValues[it.y][it.x] })
        tailrec fun goTo(current: CaveCord) {
            if (caveMap[current.y][current.x] != -1) {
                val left = CaveCord(current.x - 1, current.y)
                val right = CaveCord(current.x + 1, current.y)
                val up = CaveCord(current.x, current.y - 1)
                val down = CaveCord(current.x, current.y + 1)

                val neighbours = listOf(left, right, up, down)
                neighbours.forEach {
                    val caveMapRisk = caveMap[it.y][it.x]
                    val caveValue = caveValues[it.y][it.x]
                    if (caveValue > caveMapRisk + caveValues[current.y][current.x]) {
                        caveValues[it.y][it.x] = caveMapRisk + caveValues[current.y][current.x]
                    }
                }
                caveMap[current.y][current.x] = -1

                neighbours.filter { caveMap[it.y][it.x] != -1 }.forEach { queue.add(it) }
            }
            if (queue.isNotEmpty()) goTo(queue.remove())
        }

        val initialPosition = CaveCord(1, 1)
        goTo(initialPosition)
    }

    fun solve(input: List<String>): Int {
        //Create riskMap -- dull calculations
        val horizontalLength = input.first().length * 5
        val boundaryRow = listOf(List(horizontalLength + 2) { -1 }.toMutableList())

        val fiveTimes = (input.indices).map { y ->
            (0 until 5).map { x -> input[y].map { ((x + it.digitToInt())).let { if (it > 9) it % 10 + 1 else it } } }
                .flatten().toMutableList()
        }
        val mapped = fiveTimes.map { (listOf(-1) + it + listOf(-1)).toMutableList() }
        val bigMatrix = (0 until 5).map { x ->
            mapped.map { row ->
                row.map {
                    if (it == -1) it else (it + x).let { if (it > 9) it % 10 + 1 else it }
                }.toMutableList()
            }
        }.flatten()

        val caveMap = (boundaryRow + bigMatrix.apply { this[0][1] = 0 } + boundaryRow)
        val caveValues = caveMap.map { row -> (row.map { 1 shl 20 }).toMutableList() }.apply { this[1][1] = 0 }

        traverseGraph(caveMap, caveValues)

        return caveValues[caveMap.size - 2][horizontalLength]
    }

    val input = readInput("day15")
    println(solve(input))
}