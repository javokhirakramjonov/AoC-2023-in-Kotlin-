fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line
                .first(Char::isDigit)
                .digitToInt()
                .times(10)
                .plus(
                    line
                        .last(Char::isDigit)
                        .digitToInt()
                )
        }
    }

    fun part2(input: List<String>): Int {
        val stringDigits = listOf(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
        ).mapIndexed { index, value ->
            value to index.plus(1)
        }

        val numberDigits = (1..9).map { it.toString() to it }

        val digits = stringDigits + numberDigits

        return input.sumOf {line ->
            val first = digits
                .map { it to line.indexOf(it.first) }
                .filter { it.second != -1 }
                .minBy { it.second }
                .first
                .second
                .times(10)
            val last = digits
                .map { it to line.lastIndexOf(it.first) }
                .filter { it.second != -1 }
                .maxBy { it.second }
                .first
                .second
            first + last
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
