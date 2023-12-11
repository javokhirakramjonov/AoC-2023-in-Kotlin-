fun main() {
    fun galaxyPoLongs(input: List<String>): List<Point> {
        return input.indices.flatMap { x ->
            input[x].indices.mapNotNull { y ->
                if (input[x][y] == '#') Point(x, y) else null
            }
        }
    }

    fun getSumDistances(input: List<String>, extended: Long) : Long {
        val galaxies = galaxyPoLongs(input)

        val emptyRows = BooleanArray(input.size) { true }
        val emptyColumns = BooleanArray(input.first().length) { true }

        galaxies.forEach {
            emptyRows[it.x] = false
            emptyColumns[it.y] = false
        }

        val prefSumEmptyRows = emptyRows
            .map { if(it) 1 else 0 }
            .runningFold(0, Long::plus)

        val prefSumEmptyColumns = emptyColumns
            .map { if(it) 1 else 0 }
            .runningFold(0, Long::plus)

        val prefSumFilledRows = emptyRows
            .map { if(it) 0 else 1 }
            .runningFold(0, Long::plus)

        val prefSumFilledColumns = emptyColumns
            .map { if(it) 0 else 1 }
            .runningFold(0, Long::plus)

        var sum = 0L

        for (i in galaxies.indices) {
            for (j in i.plus(1)..galaxies.lastIndex) {
                val xs = listOf(galaxies[i].x, galaxies[j].x).sorted()
                val ys = listOf(galaxies[i].y, galaxies[j].y).sorted()

                sum += listOf(
                    (prefSumEmptyRows[xs.last()] - prefSumEmptyRows[xs.first()]).times(extended),
                    (prefSumFilledRows[xs.last()] - prefSumFilledRows[xs.first()]),
                    (prefSumEmptyColumns[ys.last()] - prefSumEmptyColumns[ys.first()]).times(extended),
                    (prefSumFilledColumns[ys.last()] - prefSumFilledColumns[ys.first()]),
                ).sum()
            }
        }

        return sum
    }

    fun part1(input: List<String>): Long {
        return getSumDistances(input, 2)
    }

    fun part2(input: List<String>): Long {
        return getSumDistances(input, 1000000L)
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
