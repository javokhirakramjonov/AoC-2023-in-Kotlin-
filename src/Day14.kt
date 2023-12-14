fun main() {

    fun tiltNorth(input: Array<Array<Char>>) {
        val currentPos = IntArray(input.first().size) { -1 }

        input.indices.forEach { x ->
            input.first().indices.forEach { y ->
                when(input[x][y]) {
                    'O', '.' -> {
                        if(currentPos[y] == -1) currentPos[y] = x

                        if(input[x][y] == 'O') {
                            input[x][y] = '.'
                            input[currentPos[y] ++][y] = 'O'
                        }
                    }
                    '#' -> currentPos[y] = -1
                }
            }
        }
    }

    fun getNorthSupport(input: Array<Array<Char>>) : Int {
        return input.mapIndexed { index, chars ->
            chars.count { it == 'O' } * (input.size - index)
        }.sum()
    }

    fun part1(input: Array<Array<Char>>): Int {
        tiltNorth(input)

        return getNorthSupport(input)
    }

    fun part2(input: Array<Array<Char>>): Int {
        var current = input

        //TODO why it worked?)
        repeat(1000) {
            repeat(4) {
                tiltNorth(current)

                current = `rotate -90 Degree`(current)
            }
        }

        return getNorthSupport(current)
    }

    val input = readInputAsArray("Day14")
    part1(input).println()
    part2(input).println()
}