import java.util.*

fun main() {


    val decodeMap = mapOf(
        "0" to "0000",
        "1" to "0001",
        "2" to "0010",
        "3" to "0011",
        "4" to "0100",
        "5" to "0101",
        "6" to "0110",
        "7" to "0111",
        "8" to "1000",
        "9" to "1001",
        "A" to "1010",
        "B" to "1011",
        "C" to "1100",
        "D" to "1101",
        "E" to "1110",
        "F" to "1111"
    )

    fun countVersion(input: String): Long {
        var mutableInput = input
        var versionCount = 0L
        var topPosition = 0
        while (mutableInput.length > 9) {
            val version = mutableInput.take(3).toInt(2)
            versionCount += 1
            val type = mutableInput.drop(3).take(3).toInt(2)
            if (type == 4) {
                if(topPosition > 0) topPosition -= 1
                mutableInput = mutableInput.drop(6)
                while (mutableInput[0] != '0') {
                    mutableInput = mutableInput.drop(5)
                }
                mutableInput = mutableInput.drop(5)
            } else {

//                if(topPosition == 0) versionCount += 1
                if (mutableInput[6] == '1') {
                    val amount = mutableInput.drop(7).take(11).toInt(2)
                    mutableInput = mutableInput.drop(18)
                    return amount.toLong()
                }
                else{
                    val length = mutableInput.drop(7).take(15).toInt(2)
                    mutableInput = mutableInput.drop(22 + length)
                }
            }
        }
        return versionCount
    }

    fun evalOperation(type: Int, args: List<Long>): Long{
        return when(type){
        0 -> {
            args.fold(0L) { acc, packet -> acc + packet}
        }
        1 -> {
            args.fold(1L) { acc, packet -> acc * packet}
        }
        2 -> {
            args.minByOrNull { it }?: 0L
        }
        3 -> {
            args.maxByOrNull { it }?: 0L
        }
        5 -> {
            if(args[0]  < args[1]) 1 else 0
        }
        6 -> {
            if(args[0] > args[1]) 1 else 0
        }
        else -> {
            if(args[0] == args[1]) 1 else 0
        }
        }
    }

    fun solveInput(input: String): Long {
        println("Solving for $input")
        var mutableInput = input
        var ansList = mutableListOf<Any>()

        while (mutableInput.length > 9) {
            val version = mutableInput.take(3).toInt(2)
            val type = mutableInput.drop(3).take(3).toInt(2)
            if (type == 4) {
                var num = ""
                mutableInput = mutableInput.drop(6)
                while (mutableInput[0] != '0') {
                    num += mutableInput.drop(1).take(4)
                    mutableInput = mutableInput.drop(5)
                }
                num += mutableInput.drop(1).take(4)
                ansList.add(num.toLong(2))
                mutableInput = mutableInput.drop(5)
            } else {
                mutableInput = if (mutableInput[6] == '1') {
                    val length = mutableInput.drop(7).take(11).toInt(2)
                    ansList.add("Operation $type:$length")
                    mutableInput.drop(18)
                } else{
                    val length = mutableInput.drop(7).take(15).toInt(2)
                    val argsInSplit = countVersion(mutableInput.drop(22).take(length)).toInt()
                    ansList.add("Operation $type:${if(type in listOf(5,6,7)) 2 else argsInSplit}")
                    mutableInput.drop(22)
                }
            }
        }

        var ans = 0L
        var myargs = mutableListOf<Long>()
        ansList.reversed().forEach {
            if(it is Long) myargs.add(it)
            else{
                val (operation, amount) = (it as String).split(" ")[1].split(":").map { it.toInt() }
                val evaluated = evalOperation(operation, myargs.takeLast(amount))
                myargs = myargs.dropLast(amount).toMutableList()
                ans = evaluated
                myargs.add(evaluated)
            }
        }
        return ans
    }
        fun part2(input: List<String>): Long {
            val number = input[0].map { decodeMap["$it"]!! }.joinToString("")
            return solveInput(number)
        }

        val input = readInput("day16")
        println(part2(input))
    }