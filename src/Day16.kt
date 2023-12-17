import java.util.*

fun main() {

    val VERTICAL = '|'
    val HORIZONTAL = '-'
    val PLUS_90 = '\\'
    val MINUS_90 = '/'
    val FLOOR = '.'
    val ENERGIZED = '#'

    fun nextPos(current: Point, currentSign: Char, lastDir: Dir): List<Pair<Point, Dir>> {
        return when (currentSign) {
            VERTICAL -> when (lastDir) {
                Dir.UP -> listOf(
                    current.copy(x = current.x - 1) to lastDir
                )

                Dir.DOWN -> listOf(
                    current.copy(x = current.x + 1) to lastDir
                )

                Dir.RIGHT, Dir.LEFT -> listOf(
                    current.copy(x = current.x - 1) to Dir.UP,
                    current.copy(x = current.x + 1) to Dir.DOWN,
                )
                else -> emptyList()
            }

            HORIZONTAL -> when (lastDir) {
                Dir.LEFT -> listOf(
                    current.copy(y = current.y - 1) to lastDir
                )

                Dir.RIGHT -> listOf(
                    current.copy(y = current.y + 1) to lastDir
                )

                Dir.UP, Dir.DOWN -> listOf(
                    current.copy(y = current.y - 1) to Dir.LEFT,
                    current.copy(y = current.y + 1) to Dir.RIGHT,
                )
                else -> emptyList()
            }

            PLUS_90 -> when (lastDir) {
                Dir.UP -> listOf(
                    current.copy(y = current.y - 1) to Dir.LEFT
                )
                Dir.RIGHT -> listOf(
                    current.copy(x = current.x + 1) to Dir.DOWN
                )
                Dir.DOWN -> listOf(
                    current.copy(y = current.y + 1) to Dir.RIGHT
                )
                Dir.LEFT -> listOf(
                    current.copy(x = current.x - 1) to Dir.UP
                )
                else -> emptyList()
            }

            MINUS_90 -> when (lastDir) {
                Dir.UP -> listOf(
                    current.copy(y = current.y + 1) to Dir.RIGHT
                )
                Dir.RIGHT -> listOf(
                    current.copy(x = current.x - 1) to Dir.UP
                )
                Dir.DOWN -> listOf(
                    current.copy(y = current.y - 1) to Dir.LEFT
                )
                Dir.LEFT -> listOf(
                    current.copy(x = current.x + 1) to Dir.DOWN
                )
                else -> emptyList()
            }

            FLOOR -> when (lastDir) {
                Dir.UP -> listOf(
                    current.copy(x = current.x - 1) to lastDir
                )
                Dir.RIGHT -> listOf(
                    current.copy(y = current.y + 1) to lastDir
                )
                Dir.DOWN -> listOf(
                    current.copy(x = current.x + 1) to lastDir
                )
                Dir.LEFT -> listOf(
                    current.copy(y = current.y - 1) to lastDir
                )
                else -> emptyList()
            }

            else -> emptyList()
        }
    }

    fun energize(p: Point, lastDir: Dir, map: List<String>): List<String> {
        val newMap = Array(map.size) { map[it].toCharArray() }

        val queue: Queue<Pair<Point, Dir>> = LinkedList()
        val used = mutableSetOf<Pair<Point, Dir>>()

        queue.add(p to lastDir)

        while (queue.isNotEmpty()) {
            val (point, lastDirection) = queue.poll()

            newMap[point.x][point.y] = ENERGIZED

            val nextPositions = nextPos(point, map[point.x][point.y], lastDirection)
                .filter {
                    it.first.x in map.indices &&
                    it.first.y in map.first().indices
                }.filterNot {
                    used.contains(it)
                }

            nextPositions.forEach {
                used.add(it)
                queue.add(it)
            }
        }

        return newMap.map { it.concatToString() }
    }

    fun part1(input: List<String>): Int {
        return energize(Point(0, 0), Dir.RIGHT, input)
            .sumOf {
                it.count {
                    it == ENERGIZED
                }
            }
    }

    fun part2(input: List<String>): Int {
        return input.indices.maxOf { x ->
            input[x].indices.maxOf { y ->
                val canStart = x == 0 || x == input.lastIndex || y == 0 || y == input[x].lastIndex

                if(canStart) Dir.entries.maxOf {
                    energize(Point(x, y), it, input)
                        .sumOf {
                            it.count {
                                it == ENERGIZED
                            }
                        }
                } else 0
            }
        }
    }

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

enum class Dir {
    UP, RIGHT, DOWN, LEFT, NO_DIRECTION
}