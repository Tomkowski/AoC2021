fun main() {
    fun countFish(fish: Int, days: Int): Long {
        val dayMap = mutableMapOf<Int, Long>()
        fun countDescendants(days: Int): Long {
            if (days < -9) return 0L
            if(dayMap[days] == null){
                dayMap[days] = 1L + (0..days step 7).fold(0L) { acc, elem -> acc + countDescendants(days - elem - 9) }
            }
            return dayMap[days]!!
        }

        return 1L + countDescendants(days - fish - 9) + (7 + fish..days step 7).fold(0L) { acc, elem -> acc + countDescendants(
            days - elem - 9
        ) }
    }

    fun part1(input: List<String>): Long {
        val fishes = input[0].split(",").map { it.toInt() }

        return fishes.fold(0L) { acc, fish -> acc + countFish(fish, 79) }
    }

    fun part2(input: List<String>): Long {
        val fishes = input[0].split(",").map { it.toInt() }

        return fishes.fold(0L) { acc, fish -> acc + countFish(fish, 255) }
    }

    val input = readInput("day06")
    println(part1(input))
    println(part2(input))
}