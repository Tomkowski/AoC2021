fun main() {

    val neighbours = mutableMapOf<String, MutableList<String>>().withDefault { emptyList<String>().toMutableList() }
    fun dfs(node: String, count: Int, visited: MutableMap<String, Int>, path: String ="start"): String {
        if (node == "end") return "$path;"
        if (visited[node]!! == 0) return ""

        if (node == "start") visited[node] = 0
        else
            if (node >= "a") {
                visited[node] = visited[node]!!.minus(1)
            }
        return neighbours[node]!!.joinToString("") { dfs(it, count, visited.toMutableMap(), "$path,$it") }
    }

    fun part1(input: List<String>): Int {
        input.forEach {
            val (key, value) = it.split("-")

            if (neighbours[key] != null) neighbours[key]!!.add(value) else neighbours[key] =
                emptyList<String>().toMutableList().apply { add(value) }
            if (neighbours[value] != null) neighbours[value]!!.add(key) else neighbours[value] =
                emptyList<String>().toMutableList().apply { add(key) }
        }
        println(neighbours)

        return dfs("start", 0, neighbours.mapValues { 1 }.toMutableMap()).split(";").toSet().filter { it.isNotEmpty() }.size
    }

    fun part2(input: List<String>): Int {
        val allPaths =  neighbours.keys.filter { it > "a" && it != "start" && it != "end"  }.map{smallCave -> dfs("start", 0, neighbours.mapValues { 1 }.toMutableMap().apply { this[smallCave] = 2 }, "start")}
        return allPaths.flatMap { it.split(";") }.filter { it.isNotEmpty() }.toSet().size
    }

    val input = readInput("day12")
    println(part1(input))
    println(part2(input))
}