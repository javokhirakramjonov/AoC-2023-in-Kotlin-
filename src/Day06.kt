import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getNumbersAfterPrefix(prefix: String, line: String) : List<Long> {
        return line
            .substring(prefix.length)
            .trim()
            .split(" ")
            .filter(String::isNotEmpty)
            .map(String::toLong)
    }

    fun getTimes(line: String) : List<Long> {
        val timePrefix = "Time:"
        return getNumbersAfterPrefix(timePrefix, line)
    }

    fun getDistances(line: String) : List<Long> {
        val distancePrefix = "Distance:"
        return getNumbersAfterPrefix(distancePrefix, line)
    }

    fun calculate(time: Long, distance: Long) : Long {
        var left = 0L
        var right = time / 2

        var m: Long

        while(left < right) {
            m = left + (right - left) / 2

            val result = m * (time - m)
            if(result <= distance) {
                left = m + 1
            } else {
                right = m
            }
        }

        return when {
            left * (time - left) <= distance -> 0
            else -> (time + 1 - left * 2)
        }
    }

    fun part1(input: List<String>): Long {
        val times = getTimes(input.first())
        val distances = getDistances(input.last())

        return times
            .zip(distances)
            .map { calculate(it.first, it.second) }
            .fold(1, Long::times)
    }

    fun part2(input: List<String>): Long {
        val times = getTimes(input.first())
        val distances = getDistances(input.last())

        val time = times.joinToString("").toLong()
        val distance = distances.joinToString("").toLong()

        return calculate(time, distance)
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}