import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun updateDistance(
        movement: Pair<Point3D, Int>,
        dp: Array<Array<IntArray>>,
        priorityQueue: PriorityQueue<Pair<Point3D, Int>>,
    ) {

        dp[movement.first.z][movement.first.x][movement.first.y].let {
            if (it == -1 || movement.second < it) {
                dp[movement.first.z][movement.first.x][movement.first.y] = movement.second

                priorityQueue.add(movement)
            }
        }

    }

    fun getPathSum(
        map: List<List<Int>>,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int
    ): Int {
        return when {
            x1 == x2 -> (min(y1, y2)..max(y1, y2)).sumOf { map[x1][it] } - map[x1][y1]
            y1 == y2 -> (min(x1, x2)..max(x1, x2)).sumOf { map[it][y1] } - map[x1][y1]
            else -> 0
        }
    }

    fun getMinPath(
        minInSameDirection: Int,
        maxInSameDirection: Int,
        map: List<List<Int>>,
        startPoint: Point,
    ): Int {

        val priorityQueue = PriorityQueue<Pair<Point3D, Int>> { m1, m2 -> m1.second.compareTo(m2.second) }

        //top, right, bottom, left
        val dp = Array(4) {
            Array(map.size) {
                IntArray(map.first().size) {
                    -1
                }
            }
        }

        val directions = arrayOf(
            arrayOf(1, 3).map { z ->
                (minInSameDirection..maxInSameDirection).map {
                    Point3D(-it, 0, z)
                }
            },
            arrayOf(0, 2).map { z ->
                (minInSameDirection..maxInSameDirection).map {
                    Point3D(0, it, z)
                }
            },
            arrayOf(1, 3).map { z ->
                (minInSameDirection..maxInSameDirection).map {
                    Point3D(it, 0, z)
                }
            },
            arrayOf(0, 2).map { z ->
                (minInSameDirection..maxInSameDirection).map {
                    Point3D(0, -it, z)
                }
            },
        )

        dp.indices.forEach {
            updateDistance(
                Pair(Point3D(startPoint.x, startPoint.y, it), 0),
                dp,
                priorityQueue
            )
        }

        while (priorityQueue.isNotEmpty()) {
            val movement = priorityQueue.poll()

            directions[movement.first.z].flatMap {
                it.map {
                    it.copy(
                        x = it.x + movement.first.x,
                        y = it.y + movement.first.y,
                    )
                }
            }.filter {
                it.x in map.indices && it.y in map.first().indices
            }.map {
                val pathSum: Int = getPathSum(map, movement.first.x, movement.first.y, it.x, it.y)

                it to pathSum + movement.second
            }.forEach {
                updateDistance(it, dp, priorityQueue)
            }
        }

        return dp.minOf { it.last().last() }
    }

    fun part1(input: List<String>): Int {


        val map = input.map { it.map { it.digitToInt() } }
        val ans = getMinPath(1, 3, map, Point(0, 0))

        return ans
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.map { it.digitToInt() } }
        val ans = getMinPath(4, 10, map, Point(0, 0))

        return ans
    }

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
)