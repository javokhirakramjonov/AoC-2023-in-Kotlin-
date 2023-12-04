fun main() {
    fun getWinningNumbers(line: String) : Set<Int> {
        return line
            .substring(
                line.indexOf(":") + 1,
                line.indexOf("|")
            )
            .split(" ")
            .filter(String::isNotEmpty)
            .map(String::toInt)
            .toSet()
    }

    fun getMyNumbers(line: String) : List<Int> {
        return line
            .substring(line.indexOf("|") + 1)
            .split(" ")
            .filter(String::isNotEmpty)
            .map(String::toInt)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val winningNumbers = getWinningNumbers(line)

            val myNumbers = getMyNumbers(line)

            val count = myNumbers.count(winningNumbers::contains)

            if(count == 0) 0 else (1 shl (count - 1))
        }
    }

    fun part2(input: List<String>): Int {
        val counts = input.mapIndexed { index, line ->
            val winningNumbers = getWinningNumbers(line)

            val myNumbers = getMyNumbers(line)

            val count = myNumbers.count(winningNumbers::contains)

            index.plus(1) to count
        }

        val copyCardCounts = IntArray(input.size + 1) { 1 }

        counts.forEach {
            (it.first.plus(1)..it.first.plus(it.second)).forEach { copyCard ->
                if(copyCard < copyCardCounts.size)
                    copyCardCounts[copyCard] += copyCardCounts[it.first]
            }
        }

        return copyCardCounts.drop(1).sum()
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
