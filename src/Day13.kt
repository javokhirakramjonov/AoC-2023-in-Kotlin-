fun main() {
    fun extractMirrors(input: List<String>): List<List<String>> {
        return input
            .joinToString("\n")
            .split("\n\n")
            .map { it.split("\n") }
    }

    fun getMaxVerticalReflectionLength(input: List<CharArray>, chance: Int): Int {
        return input.first().indices.indexOfFirst {
            var l = it
            var r = it + 1
            var chances = chance

            big@ while (l >= 0 && r <= input.first().lastIndex) {
                for (j in input.indices) {
                    if (input[j][l] != input[j][r] && --chances < 0) break@big
                }

                l--
                r++
            }

            (l == -1 || r == input.first().size) && chances == 0
        } + 1
    }

    fun getMaxHorizontalReflectionLength(input: List<CharArray>, chance: Int): Int {
        return input.indices.indexOfFirst {
            var l = it
            var r = it + 1
            var chances = chance

            big@ while (l >= 0 && r <= input.lastIndex) {
                for (j in input.first().indices) {
                    if (input[l][j] != input[r][j] && --chances < 0) break@big
                }

                l--
                r++
            }

            (l == -1 || r == input.size) && chances == 0
        } + 1
    }

    fun part1(input: List<String>): Int {
        return extractMirrors(input).sumOf { partActual ->
            val part = partActual.map { it.toCharArray() }

            val vertical = getMaxVerticalReflectionLength(part, 0)
            val horizontal = getMaxHorizontalReflectionLength(part, 0)

            if (vertical == part.first().size) horizontal * 100 else vertical
        }
    }

    fun part2(input: List<String>): Int {
        return extractMirrors(input).sumOf { partActual ->
            val part = partActual.map { it.toCharArray() }

            val vertical = getMaxVerticalReflectionLength(part, 1)
            val horizontal = getMaxHorizontalReflectionLength(part, 1)

            if (vertical == 0) horizontal * 100 else vertical
        }
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}