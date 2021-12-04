/**
 * INPUT FROM ADVENT OF CODE HAS TO BE REFACTORED FOR THIS DAY.
 * REPLACE ALL DOUBLE SPACES WITH SINGLE SPACE: "  " -> " "
 * REMOVE ALL SPACES AT THE BEGINNING OF EACH LINE: "^ " -> ""
 */

fun main() {

    fun checkBingo(board: List<List<Int>>): Boolean {
        board.forEach { row -> if (row.sum() == -5) return true }
        (0 until 5).forEach { number ->
            if (board.map { row -> row[number] }.sum() == -5) return true
        }
        return false
    }

    fun countPointsOnBoard(board: List<List<Int>>): Int {
        return board.foldRight(0) { row, acc -> acc + row.map { if (it == -1) 0 else it }.sum() }
    }

    fun playBingo(board: List<List<Int>>, numbers: List<Int>): Pair<List<List<Int>>, Int> {
        val drawnNumber = numbers[0]
        val updated = board.map { row -> row.map { if (it == drawnNumber) -1 else it } }
        if (checkBingo(updated)) {
            return updated to countPointsOnBoard(updated) * drawnNumber
        }
        return updated to -1
    }

    fun findWinner(boards: List<List<List<Int>>>, bingoNumbers: List<Int>): Int {
        if (bingoNumbers.isEmpty()) return -1
        val updated = boards.map {
            val (updatedBoard, result) = playBingo(it, bingoNumbers)
            if (result != -1) return result
            updatedBoard
        }
        return findWinner(updated, bingoNumbers.drop(1))
    }

    fun part1(input: List<String>): Int {
        val data = input.joinToString("\n").split("\n")
        val bingoNumbers = data[0].split(",").map { it.toInt() }

        val boardsNumber = ((input.size - 2) / 6) // 2 lines for bingo numbers. Each table takes 5 lines and 1 separator
        val boards = (0 until boardsNumber).map {
            data.subList(it * 6 + 2, it * 6 + 7).map { row -> row.split(" ").map { number -> number.toInt() } }
        }
        return findWinner(boards, bingoNumbers)
    }

    fun findLoser(boards: List<List<List<Int>>>, bingoNumbers: List<Int>, answer: Int): Int{
        var newestResult = 0
        val wonBoards = mutableListOf<Int>()

        if (bingoNumbers.isEmpty() || boards.isEmpty()) return answer
        val updated = boards.mapIndexed { index, it ->
            val (updatedBoard, result) = playBingo(it, bingoNumbers)
            if (result != -1) {
                newestResult = result
                wonBoards.add(index)
            }
            updatedBoard
        }
        return findLoser(updated.filterIndexed { index, _ -> !wonBoards.contains(index) }, bingoNumbers.drop(1), newestResult)
    }

    fun part2(input: List<String>): Int {
        val data = input.joinToString("\n").split("\n")
        val bingoNumbers = data[0].split(",").map { it.toInt() }

        val boardsNumber = ((input.size - 2) / 6)
        val boards = (0 until boardsNumber).map {
            data.subList(it * 6 + 2, it * 6 + 7).map { row -> row.split(" ").map { number -> number.toInt() } }
        }
        return findLoser(boards, bingoNumbers, 0)
    }

    val input = readInput("day04")
    println(part1(input))
    println(part2(input))
}