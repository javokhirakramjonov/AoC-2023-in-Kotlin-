fun main() {

    val dx = arrayOf(-1, 0, 1, 0)
    val dy = arrayOf(0, 1, 0, -1)

    var isSlipEnabled = false
    var map: List<String> = emptyList()
    var used: Array<BooleanArray> = emptyArray()

    fun dfs(current: Point): Int {
        if(current.x == map.lastIndex) return 0

        used[current.x][current.y] = true

        val dirs = if (isSlipEnabled)
            when (map[current.x][current.y]) {
                '^' -> 0..0
                '>' -> 1..1
                'v' -> 2..2
                '<' -> 3..3
                else -> 0..3
            }
        else
            0..3

        val res = dirs
            .map {
                current.copy(
                    x = current.x + dx[it],
                    y = current.y + dy[it]
                )
            }
            .filter {
                it.x in map.indices &&
                        it.y in map.first().indices &&
                        !used[it.x][it.y] &&
                        map[it.x][it.y] != '#'
            }
            .maxOfOrNull(::dfs)
            ?: -1

        used[current.x][current.y] = false

        return res + 1
    }

    fun part1(input: List<String>): Int {
        map = input
        isSlipEnabled = true
        used = Array(map.size) { BooleanArray(map.first().length) }
        return dfs(Point(0, 1))
    }

    fun part2(input: List<String>): Int {
        map = input
        isSlipEnabled = false
        used = Array(map.size) { BooleanArray(map.first().length) }
        return dfs(Point(0, 1))
    }

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}