fun main() {

    tailrec fun getNextNumber(
        nums: List<Int>,
        sum: Int = 0
    ): Int {
        if (nums.size == 1) return nums.first()
        if (nums.all { it == 0 }) return sum

        val diffs = nums
            .windowed(2)
            .map { it.last() - it.first() }

        return getNextNumber(diffs, nums.last() + sum)
    }

    fun getNumLists(input: List<String>): List<List<Int>> {
        return input
            .map {
                it
                    .split(" ")
                    .map(String::toInt)
            }
    }

    fun part1(input: List<String>): Int {
        return getNumLists(input)
            .map { getNextNumber(it) }
            .fold(0, Int::plus)
    }

    fun part2(input: List<String>): Int {
        return getNumLists(input)
            .map { it.reversed() }
            .map { getNextNumber(it) }
            .fold(0, Int::plus)
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}