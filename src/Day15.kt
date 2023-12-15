fun main() {

    fun extractWords(input: List<String>) : List<String> {
        return input
            .joinToString("")
            .split(",")
            .toList()
    }

    fun nextHashValue(current: Int, char: Char) : Int {
        return (current + char.code) * 17 % 256
    }

    fun part1(input: List<String>): Int {
        return extractWords(input)
            .sumOf {
                val initial = 0

                it.fold(initial) { acc, char ->
                    nextHashValue(acc, char)
                }
            }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}