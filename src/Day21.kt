import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        var limit = 64

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

        return map.sumOf {
            it.count { it == 'O' }
        }
    }

    fun lagrange(points: List<Point>, x: Long): Long {
        val x1 = points[0].x;
        val y1 = points[0].y;
        val x2 = points[1].x;
        val y2 = points[1].y;
        val x3 = points[2].x;
        val y3 = points[2].y;

        return (((x - x2) * (x - x3)) / ((x1 - x2) * (x1 - x3)) * y1 +
                ((x - x1) * (x - x3)) / ((x2 - x1) * (x2 - x3)) * y2 +
                ((x - x1) * (x - x2)) / ((x3 - x1) * (x3 - x2)) * y3)
    }

    fun part2(input: List<String>): Long {

        val n = 26501365
        val width = input.first().length
        val height = input.size
        val cycle = (n / width).toLong()
        val remainder = n % width

        val xS = input.indexOfFirst { it.contains('S') }
        val yS = input[xS].indexOf('S')

        var set = setOf(Point(xS, yS))

        val points = mutableListOf<Point>()

        var step = 0

        repeat(3) {
            while(step ++ < it * width + remainder) {
                val dx = listOf(-1, 0, 1, 0)
                val dy = listOf(0, 1, 0, -1)

                set = set
                    .flatMap {
                        dx
                            .zip(dy) { x, y ->
                                Point(x + it.x, y + it.y)
                            }
                            .filter {
                                val newX = (it.x % height + height) % height
                                val newY = (it.y % width + width) % width

                                input[newX][newY] != '#'
                            }
                    }
                    .toSet()
            }

            points += Point(it, set.size)
        }

        return lagrange(points, cycle)
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}