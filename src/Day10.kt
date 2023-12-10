import java.util.*

fun main() {

    val VERTICAL = '|'
    val HORIZONTAL = '-'
    val LEFT_BOTTOM = 'L'
    val RIGHT_BOTTOM = 'J'
    val RIGHT_TOP = '7'
    val LEFT_TOP = 'F'
    val FLOOR = '.'
    val START = 'S'

    val xDirs = arrayOf(-1, 0, 1, 0)
    val yDirs = arrayOf(0, 1, 0, -1)

    val nextPos = mapOf<Char, (prevPos: Point, currentPos: Point) -> Point?>(
        LEFT_TOP to { prev, current ->
            when {
                prev.x == current.x + 1 && prev.y == current.y -> Point(current.x, current.y + 1)
                prev.x == current.x && prev.y == current.y + 1 -> Point(current.x + 1, current.y)
                else -> null
            }
        },
        RIGHT_TOP to { prev, current ->
            when {
                prev.x == current.x + 1 && prev.y == current.y -> Point(current.x, current.y - 1)
                prev.x == current.x && prev.y == current.y - 1 -> Point(current.x + 1, current.y)
                else -> null
            }
        },
        LEFT_BOTTOM to { prev, current ->
            when {
                prev.x == current.x - 1 && prev.y == current.y -> Point(current.x, current.y + 1)
                prev.x == current.x && prev.y == current.y + 1 -> Point(current.x - 1, current.y)
                else -> null
            }
        },
        RIGHT_BOTTOM to { prev, current ->
            when {
                prev.x == current.x - 1 && prev.y == current.y -> Point(current.x, current.y - 1)
                prev.x == current.x && prev.y == current.y - 1 -> Point(current.x - 1, current.y)
                else -> null
            }
        },
        HORIZONTAL to { prev, current ->
            when {
                prev.x == current.x && prev.y == current.y + 1 -> Point(current.x, current.y - 1)
                prev.x == current.x && prev.y == current.y - 1 -> Point(current.x, current.y + 1)
                else -> null
            }
        },
        VERTICAL to { prev, current ->
            when {
                prev.x == current.x + 1 && prev.y == current.y -> Point(current.x - 1, current.y)
                prev.x == current.x - 1 && prev.y == current.y -> Point(current.x + 1, current.y)
                else -> null
            }
        },
        FLOOR to { _, _ -> null }
    )

    fun getShape(input: List<String>): Array<BooleanArray> {
        val x = input.indexOfFirst { it.contains(START) }
        val y = input[x].indexOf(START)

        val used = Array<BooleanArray>(input.size) {
            BooleanArray(input[it].length) {
                false
            }
        }

        val queue: Queue<Pair<Point, Point>> = LinkedList()

        queue += Point(-1, -1) to Point(x, y)
        used[x][y] = true

        while (queue.isNotEmpty()) {
            val (pre, curr) = queue.poll()

            val char = input[curr.x][curr.y]

            val nextPoints = if (char == START) {
                xDirs
                    .zip(yDirs)
                    .map {
                        Point(
                            it.first + curr.x,
                            it.second + curr.y
                        )
                    }
                    .filter {
                        it.x in input.indices && it.y in input[it.x].indices
                    }
                    .filter {
                        nextPos.getValue(input[it.x][it.y])(curr, it) != null
                    }
            } else {
                listOfNotNull(nextPos.getValue(char)(pre, curr))
            }

            nextPoints.filter {
                it.x in input.indices &&
                        it.y in input[it.x].indices &&
                        !used[it.x][it.y] &&
                        input[it.x][it.y] != FLOOR
            }.forEach {
                used[it.x][it.y] = true
                queue += curr to it
            }
        }

        return used
    }

    fun part1(input: List<String>): Int {
        val shape = getShape(input)
        val perimeter = shape.sumOf {
            it.count { it }
        }

        return perimeter / 2
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class Point(
    val x: Int,
    val y: Int
)