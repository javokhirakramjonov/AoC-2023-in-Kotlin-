import java.awt.Point

fun main() {
    val xDir = intArrayOf(-1, 0, 1)
    val yDir = intArrayOf(-1, 0, 1)

    fun part1(input: List<String>): Int {
        return input.mapIndexed { lineIndex, line ->
            var lineSum = 0
            var number = 0
            var shouldInclude = false

            line.forEachIndexed { charIndex, c ->
                if (c.isDigit()) {
                    shouldInclude = shouldInclude || xDir.any { x ->
                        yDir.any { y ->
                            val sign = input.getOrNull(lineIndex + x)?.getOrNull(charIndex + y) ?: '.'
                            sign != '.' && !sign.isDigit()
                        }
                    }

                    number = number * 10 + c.digitToInt()
                } else {
                    lineSum += number.takeIf { shouldInclude } ?: 0
                    number = 0
                    shouldInclude = false
                }
            }

            lineSum + (number.takeIf { shouldInclude } ?: 0)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '*') continue

                val numbers = mutableListOf<Int>()
                val used: Array<BooleanArray> = Array(3) { BooleanArray(input[i].length) }

                xDir.forEach { x ->
                    yDir.forEach { y ->
                        val point = Point(i + x, j + y)

                        val sign = input.getOrNull(point.x)?.getOrNull(point.y) ?: '.'

                        if (sign.isDigit() && !used[point.x - (i - 1)][point.y]) {
                            val numberString = buildString {
                                for (ii in point.y downTo 0) {
                                    if (input[point.x][ii].isDigit()) {
                                        insert(0, input[point.x][ii])
                                        used[point.x - (i - 1)][ii] = true
                                    } else break
                                }
                                for (ii in point.y + 1..input[i].lastIndex) {
                                    if (input[point.x][ii].isDigit()) {
                                        append(input[point.x][ii])
                                        used[point.x - (i - 1)][ii] = true
                                    } else break
                                }
                            }

                            numbers += numberString.toInt()
                        }
                    }
                }

                sum += numbers.takeIf { it.size == 2 }?.reduce(Int::times) ?: 0
            }
        }

        return sum
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
