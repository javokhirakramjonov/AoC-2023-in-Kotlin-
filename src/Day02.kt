fun main() {
    fun lineToIdAndMaxCountedCubes(line: String): Pair<Int, List<Int>> {
        val prefix = "Game "
        val endOfId = ":"
        val indexOfEndOfId = line.indexOf(endOfId)

        val id = line.substring(prefix.length, indexOfEndOfId).toInt()

        val spliteratorOfCountAndColor = " "

        val cubes = line
            .substring(indexOfEndOfId + endOfId.length)
            .replace(';', ',')
            .split(',')
            .map(String::trim)
            .map {
                val indexOfSpliterator = it.indexOf(spliteratorOfCountAndColor)
                val color = it.substring(indexOfSpliterator + spliteratorOfCountAndColor.length)
                val count = it.substring(0, indexOfSpliterator).toInt()

                Pair(color, count)
            }

        val maxGreen = cubes.filter { it.first == "green" }.maxOf { it.second }
        val maxRed = cubes.filter { it.first == "red" }.maxOf { it.second }
        val maxBlue = cubes.filter { it.first == "blue" }.maxOf { it.second }

        return id to listOf(
            maxGreen,
            maxRed,
            maxBlue
        )
    }

    fun part1(input: List<String>): Int {
        val allGreen = 13
        val allRed = 12
        val allBlue = 14

        val alls = listOf(allGreen, allRed, allBlue)

        return input
            .map(::lineToIdAndMaxCountedCubes)
            .sumOf {

                if (
                    !(alls.indices).any { index -> alls[index] < it.second[index] }
                )
                    it.first
                else
                    0
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .map(::lineToIdAndMaxCountedCubes)
            .map { it.second }
            .sumOf {
                it.fold(1, Int::times)
            }
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
