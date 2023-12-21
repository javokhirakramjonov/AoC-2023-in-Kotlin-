import java.util.*
import kotlin.collections.HashSet

fun main() {

    fun part1(input: List<String>): Int {
        var limit = 65

        val queue: Queue<Pair<Point, Char>> = LinkedList()

        val sX = input.indexOfFirst { it.contains('S') }
        val sY = input[sX].indexOf('S')

        val used = HashSet<Point>()

        used.add(Point(sX, sY))
        queue.add(Point(sX, sY) to 'O')

        val map = input.map { it.toCharArray() }.toTypedArray()

        val dx = listOf(-1, 0, 1, 0)
        val dy = listOf(0, 1, 0, -1)

        while (queue.isNotEmpty()) {

            var size = queue.size

            while (size-- > 0) {
                val current = queue.poll()

                map[current.first.x][current.first.y] = current.second

                dx.zip(dy).map {
                    current.first.copy(
                        x = current.first.x + it.first,
                        y = current.first.y + it.second
                    )
                }.filter {
                    it.x in input.indices &&
                            it.y in input.first().indices &&
                            input[it.x][it.y] == '.' &&
                            !used.contains(Point(it.x, it.y))
                }.map {
                    Point(it.x, it.y) to if (current.second == 'O') '.' else 'O'
                }.forEach {
                    used.add(it.first)
                    queue.add(it)
                }
            }

            if (limit-- == 0) break
        }

        println(map.joinToString("\n") { it.joinToString(" ") })

        return map.sumOf {
            it.count { it == 'O' }
        }
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}