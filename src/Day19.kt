import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val ACCEPT = "A"
    val REJECT = "R"

    fun mapToFilterSets(input: List<String>): List<FilterSet> {
        return input.map {
            val name = it.substringBefore("{").trim()
            val filters = it
                .substringAfter("{")
                .substringBefore("}")
                .split(",")
                .map {
                    val successAddress: String
                    var partName: String? = null
                    var minVal: Int? = null
                    var maxVal: Int? = null

                    if (it.contains(':')) {
                        val temp = it.substringBefore(':').split("[<>]".toRegex())
                        successAddress = it.substringAfter(':')
                        partName = temp.first()
                        if (it.contains('>')) minVal = temp.last().toInt() + 1
                        if (it.contains('<')) maxVal = temp.last().toInt() - 1
                    } else {
                        successAddress = it
                    }

                    Filter(successAddress, partName, minVal, maxVal)
                }

            FilterSet(name, filters)
        }
    }

    fun mapToPartValuesList(input: List<String>): List<List<Part>> {
        return input.map {
            it
                .substringAfter('{')
                .substringBefore('}')
                .split(',')
                .map {
                    val temp = it.split('=')
                    Part(temp.first(), temp.last().toInt())
                }
        }
    }

    fun part1(input: List<String>): Int {
        val separatedIndex = input.indexOf("")
        val filterSets = mapToFilterSets(input.subList(0, separatedIndex + 1))
        val partValuesList = mapToPartValuesList(input.subList(separatedIndex + 1, input.size))

        return partValuesList
            .filter {
                var current = "in"

                do {
                    val filterSet = filterSets.first { it.name == current }
                    current = filterSet.filters.first { filterer ->
                        if (filterer.partName == null) true else
                            filterer.filter(it.first { it.name == filterer.partName }.value)
                    }.successAddress
                } while (current != ACCEPT && current != REJECT)

                current == ACCEPT
            }
            .sumOf {
                it.sumOf { it.value }
            }
    }

    fun getNewRange(currentRange: IntRange, minVal: Int?, maxVal: Int?): IntRange? {
        val newMin = if (minVal == null) currentRange.first else max(currentRange.first, minVal)
        val newMax = if (maxVal == null) currentRange.last else min(currentRange.last, maxVal)

        return if (newMin <= newMax) newMin..newMax else null
    }

    fun getAllPossibleRangeSets(filterSets: List<FilterSet>): List<List<PartWithRange>> {
        val queue: Queue<Pair<FilterSet, List<PartWithRange>>> = LinkedList()
        val ans = HashSet<List<PartWithRange>>()
        val used = HashSet<Pair<FilterSet, List<PartWithRange>>>()

        val inFilter = filterSets.first { it.name == "in" }
        var pair = inFilter to listOf("x", "m", "a", "s").map { PartWithRange(it, 1..4000) }

        queue.add(pair)
        used.add(pair)

        while (queue.isNotEmpty()) {
            pair = queue.poll()

            var currentRanges = pair.second

            pair.first.filters.forEach { filter ->
                var newParts: List<PartWithRange>? = null

                if (filter.partName == null) {
                    newParts = currentRanges
                } else {
                    val part = currentRanges.first { it.name == filter.partName }

                    val newRange = getNewRange(part.range, filter.minVal, filter.maxVal)

                    if (newRange != null) {
                        val antiRange =
                            if (part.range.first == newRange.first) newRange.last.plus(1)..part.range.last else part.range.first..newRange.first.minus(
                                1
                            )
                        newParts = currentRanges.map { if (it == part) part.copy(range = newRange) else it }
                        currentRanges = currentRanges.map { if (it == part) part.copy(range = antiRange) else it }
                    }
                }

                if (newParts != null) {
                    when (filter.successAddress) {
                        ACCEPT -> ans.add(newParts)
                        REJECT -> return@forEach
                        else -> {
                            val successAddress = filterSets.first { it.name == filter.successAddress }
                            val newPair = successAddress to newParts

                            if (!used.contains(newPair)) {
                                used.add(newPair)
                                queue.add(newPair)
                            }
                        }
                    }
                }

                if (filter.partName == null) return@forEach
            }
        }

        return ans.toList()
    }

    fun part2(input: List<String>): Long {
        val separatedIndex = input.indexOf("")
        val filterSets = mapToFilterSets(input.subList(0, separatedIndex + 1))

        val ranges = getAllPossibleRangeSets(filterSets)

        return ranges.sumOf {
            it
                .map { it.range.last - it.range.first + 1 }
                .map { it.toLong() }
                .fold(1L, Long::times)
        }
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

private data class PartWithRange(val name: String, val range: IntRange)
private data class Part(val name: String, val value: Int)
private data class Filter(val successAddress: String, val partName: String?, val minVal: Int?, val maxVal: Int?) {
    fun filter(value: Int): Boolean {
        val f1 = minVal == null || value >= minVal
        val f2 = maxVal == null || value <= maxVal

        return (f1 && f2)
    }
}

private data class FilterSet(val name: String, val filters: List<Filter>)