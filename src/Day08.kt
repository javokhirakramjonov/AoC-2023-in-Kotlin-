import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getMapValues(input: List<String>): Map<String, Pair<String, String>> {
        return input
            .filter(String::isNotBlank)
            .associate {
            val key = it.substring(0, it.indexOf("=")).trim()
            val (left, right) = it.substring(
                it.indexOf("(") + 1,
                it.indexOf(")")
            )
                .split(",")
                .map(String::trim)

            key to Pair(left, right)
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first()

        val map = getMapValues(input.drop(1))

        val start = "AAA"
        val end = "ZZZ"

        var current = start

        var step = 0

        while(current != end) {
            val next = instructions[step % instructions.length]

            current = if(next == 'L') map.getValue(current).first else map.getValue(current).second

            step ++
        }

        return step
    }

    fun gcd(a: Long, b: Long) : Long {
        var a = a
        var b = b

        while(b != 0L) {
            val c = a % b
            a = b
            b = c
        }

        return a
    }

    fun lcm(a: Long, b: Long) : Long {
        return max(a, b) / gcd(a, b) * min(a, b)
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first()

        val map = getMapValues(input.drop(1))

        val start = map.keys.filter { it.endsWith('A') }
        val finishPredicate : ((String) -> Boolean) = { it.endsWith('Z') }

        val steps = start.map {

            var current = it
            var step = 0

            while(!finishPredicate(current) || step % instructions.length > 0 ) {
                val next = instructions[step % instructions.length]

                current = if(next == 'L') map.getValue(current).first else map.getValue(current).second

                step ++
            }

            step
        }.map(Int::toLong)

        return steps.fold(1L) { currentLcm, num ->
            lcm(currentLcm, num)
        }
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}