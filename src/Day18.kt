import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getPath(input: List<String>): List<Pair<Dir, Int>> {
        return input.map {
            val (dir, count) = it
                .split(" ")
                .take(2)

            val newDir = when (dir) {
                "U" -> Dir.UP
                "R" -> Dir.RIGHT
                "D" -> Dir.DOWN
                "L" -> Dir.LEFT
                else -> Dir.NO_DIRECTION
            }

            newDir to count.toInt()
        }
    }

    fun getPath2(input: List<String>): List<Pair<Dir, Int>> {
        return input.map {
            val hex = it
                .split(" ")
                .last()
                .drop(1)
                .dropLast(1)

            val newDir = when (hex.last().digitToInt()) {
                3 -> Dir.UP
                0 -> Dir.RIGHT
                1 -> Dir.DOWN
                2 -> Dir.LEFT
                else -> Dir.NO_DIRECTION
            }

            val count = hex
                .drop(1)
                .dropLast(1)
                .toInt(16)

            newDir to count
        }
    }

    fun calculateArea(path: List<Point>): Double {
        val sum = path.indices.sumOf {
            val current = path[it]
            val next = path[(it + 1) % path.size]

            val s1 = current.x.toDouble() * next.y.toDouble()
            val s2 = next.x.toDouble() * current.y.toDouble()

            s1 - s2
        }

        return abs(sum) / 2.0
    }

    fun getInnerPoints(outPoints: Int, area: Double): Double {
        return (area - outPoints / 2.0 + 1)
    }

    fun drawMap(points: List<Point>) {
        val topLeft = Point(
            points.minOf { it.x },
            points.minOf { it.y }
        )

        val width = points.maxOf { it.y } - points.minOf { it.y } + 1
        val height = points.maxOf { it.x } - points.minOf { it.x } + 1

        val map = Array(height) { Array(width) { '.' } }

        points.forEach { map[it.x - topLeft.x][it.y - topLeft.y] = '#' }

        println(map.joinToString("\n") { it.joinToString("") })
    }

    fun getFullArea(path: List<Pair<Dir, Int>>) : Double {
        var current = Point(0, 0)
        val points = mutableListOf(current)
        var pathLength = 1

        path.forEach {
            val count = it.second

            val nextPoint = when (it.first) {
                Dir.UP -> current.copy(x = current.x - count)
                Dir.RIGHT -> current.copy(y = current.y + count)
                Dir.DOWN -> current.copy(x = current.x + count)
                Dir.LEFT -> current.copy(y = current.y - count)
                Dir.NO_DIRECTION -> current
            }

            pathLength += abs(nextPoint.x - current.x) + abs(nextPoint.y - current.y)

            current = nextPoint

            points.add(nextPoint)
        }

        return getInnerPoints(pathLength, calculateArea(points)) + pathLength
    }

    fun part1(input: List<String>): Double {
        return getFullArea(getPath(input))
    }

    fun part2(input: List<String>): Double {
        return getFullArea(getPath2(input))
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}