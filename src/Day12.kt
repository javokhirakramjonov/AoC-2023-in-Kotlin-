fun main() {

    fun getMap(line: String): String {
        return line.substring(0, line.indexOf(" "))
    }

    fun getPattern(line: String): List<Long> {
        return line
            .split(" ")
            .last()
            .split(",")
            .map(String::toLong)
    }

    fun dfs(
        pos: Int,
        map: CharArray,
        damagedCount: Long,
        patternPos: Int,
        pattern: List<Long>,
        dp: HashMap<Pair<Long, Long>, Long>
    ): Long {

        if (pos == map.size) {
            if (damagedCount == 0L && patternPos >= pattern.size) return 1
            if (patternPos == pattern.lastIndex && damagedCount == pattern[patternPos]) return 1
            return 0
        }

        when (map[pos]) {
            '#' -> {
                if (patternPos >= pattern.size || damagedCount + 1 > pattern[patternPos]) return 0L
                return dfs(
                    pos + 1,
                    map,
                    damagedCount + 1,
                    patternPos,
                    pattern,
                    dp
                )
            }

            '.' -> {
                val newPatternPos = if (map.getOrNull(pos - 1) == '#') patternPos + 1 else patternPos

                if (patternPos < pattern.size && map.getOrNull(pos - 1) == '#' && pattern[patternPos] != damagedCount) return 0L

                val key = Pair(pos.toLong(), newPatternPos.toLong())

                if (dp[key] == null) {
                    dp[key] = dfs(
                        pos + 1,
                        map,
                        0,
                        newPatternPos,
                        pattern,
                        dp
                    )
                }

                return dp.getValue(key)
            }

            else -> {
                var sum = 0L

                map[pos] = '.'

                sum += dfs(
                    pos,
                    map,
                    damagedCount,
                    patternPos,
                    pattern,
                    dp
                )

                map[pos] = '#'

                sum += dfs(
                    pos,
                    map,
                    damagedCount,
                    patternPos,
                    pattern,
                    dp
                )

                map[pos] = '?'

                return sum
            }
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf {
            val map = getMap(it).toCharArray()
            val pattern = getPattern(it)
            dfs(0, map, 0, 0, pattern, HashMap())
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val map = List(5) { getMap(line) }.joinToString("?").toCharArray()
            val pattern = List(5) { getPattern(line) }.flatten()
            dfs(0, map, 0, 0, pattern, HashMap())
        }
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
