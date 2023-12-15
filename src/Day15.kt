fun main() {

    fun extractWords(input: List<String>): List<String> {
        return input
            .joinToString("")
            .split(",")
            .toList()
    }

    fun nextHashValue(current: Int, char: Char): Int {
        return (current + char.code) * 17 % 256
    }

    fun hash(input: String): Int {
        return input.fold(0) { acc, char ->
            nextHashValue(acc, char)
        }
    }

    fun part1(input: List<String>): Int {
        return extractWords(input).sumOf(::hash)
    }

    fun part2(input: List<String>): Int {
        val map = HashMap<Int, LinkedHashMap<String, Int>>()

        extractWords(input).forEach {
            val opIndex = it.indexOfFirst { !it.isLetterOrDigit() }

            if (it[opIndex] == '=') {
                val key = it.substring(0, opIndex)
                val length = it.substring(opIndex + 1).toInt()
                val hash = hash(key)

                map.getOrPut(hash) { LinkedHashMap() }[key] = length
            } else {
                val key = it.substring(0, opIndex)
                val hash = hash(key)

                map.getOrDefault(hash, HashMap()).remove(key)
            }
        }

        return map.entries.sumOf { keyValue ->
            keyValue.key.plus(1) * keyValue.value.values.mapIndexed { index, value ->
                index.plus(1) * value
            }.sum()
        }
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}