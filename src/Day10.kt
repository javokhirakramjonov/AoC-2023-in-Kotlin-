import java.util.*
import kotlin.math.abs

fun main() {

    val VERTICAL = '|'
    val HORIZONTAL = '-'
    val LEFT_BOTTOM = 'L'
    val RIGHT_BOTTOM = 'J'
    val RIGHT_TOP = '7'
    val LEFT_TOP = 'F'
    val FLOOR = '.'
    val START = 'S'

    val xDirs = arrayOf(0, 1, 0, -1)
    val yDirs = arrayOf(1, 0, -1, 0)

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

    fun getPath(input: List<String>): List<Point> {
        val x = input.indexOfFirst { it.contains(START) }
        val y = input[x].indexOf(START)

        val used = Array<BooleanArray>(input.size) {
            BooleanArray(input[it].length) {
                false
            }
        }

        val stack: Stack<Pair<Point, Point>> = Stack()

        stack += Point(-1, -1) to Point(x, y)
        used[x][y] = true

        val path = mutableListOf<Point>()

        while (stack.isNotEmpty()) {
            val (pre, curr) = stack.pop()

            path.add(curr)

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
                stack += curr to it
            }
        }

        return path
    }

    fun part1(input: List<String>): Int {
        val path = getPath(input)

        return path.size / 2
    }

    fun calculateArea(path: List<Point>): Double {
        val sum = path.indices.sumOf {
            val current = path[it]
            val next = path[(it + 1) % path.size]

            (current.y + next.y) * (current.x - next.x)
        }

        return abs(sum) / 2.0
    }

    fun getInnerPoints(outPoints: Int, area: Double): Double {
        return (area - outPoints / 2.0 + 1)
    }

    fun part2(input: List<String>): Double {
        val path = getPath(input)
        val area = calculateArea(path)

        val innerPoints = getInnerPoints(path.size, area)

        return innerPoints
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class Point(
    val x: Int,
    val y: Int
)