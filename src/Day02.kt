fun main() {

    fun getValues(command: String, input: List<String>): List<Int> {
        return input.filter { it.startsWith(command) }.map { it.split(" ")[1].toInt() }
    }

    fun part1(input: List<String>): Int {
        val forward = getValues("forward", input)
        val down = getValues("down", input)
        val up = getValues("up", input)

        return forward.sum() * (down.sum() - up.sum())
    }

    fun part2(input: List<String>): Int {
        var aim = 0
        var horizontal = 0
        var vertical = 0
        input.forEach { command ->
                val power = command.split(" ")[1].toInt()
                if (command.startsWith("forward")) {
                    horizontal += power
                    vertical += aim * power
                }
                if (command.startsWith("down")) {
                    aim += power
                }
                if (command.startsWith("up")) {
                    aim -= power
                }
            }
        return horizontal * vertical
    }

    val input = readInput("day02")
    println(part1(input))
    println(part2(input))
}
