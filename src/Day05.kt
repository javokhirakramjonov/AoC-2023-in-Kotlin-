import kotlin.math.max
import kotlin.math.min

fun main() {

    fun getSeeds(line: String): List<Long> {
        val seeds = "seeds: "
        return line
            .substring(seeds.length)
            .split(" ")
            .map(String::toLong)
    }

    fun getFromTo(line: String): Pair<String, String> {
        val fromTo = line
            .take(line.indexOf("map:"))
            .trim()
            .split("-to-")

        return Pair(fromTo[0], fromTo[1])
    }

    fun getMappings(input: List<String>): List<Mapping> {
        return input
            .joinToString("\n")
            .split("\n\n")
            .map { it.split("\n") }
            .map {
                val (from, to) = getFromTo(it.first())

                val mapRanges = it
                    .drop(1)
                    .map { mapRange ->
                        val (fromDest, fromSrc, length) = mapRange.split(" ").map(String::toLong)
                        MapRange(fromDest, fromSrc, length)
                    }

                Mapping(
                    from = from,
                    to = to,
                    values = mapRanges
                )
            }
    }

    fun part1(input: List<String>): Long {
        val seeds = getSeeds(input.first())
        val mappings = getMappings(input.drop(2))

        val from = "seed"
        val to = "location"

        var current = from
        var values = seeds

        while (current != to) {
            val mapping = mappings.first { it.from == current }
            current = mapping.to
            values = values.map { value ->
                val result = mapping
                    .values
                    .firstOrNull { value in it.fromSrc..it.fromSrc.plus(it.length) }
                    ?.let {
                        it.fromDest + value - it.fromSrc
                    } ?: value

                result
            }
        }

        return values.min()
    }

    fun mergeOverlappedRanges(list: List<Range>): List<Range> {
        if(list.isEmpty()) return emptyList()
        val result = mutableListOf<Range>()

        val sorted = list.sortedBy { it.from }
        var current = sorted.first()

        sorted.drop(1).forEach {
            current = if (it.from > current.to) {
                result.add(current)
                it
            } else {
                Range(current.from, max(current.to, it.to))
            }
        }

        result.add(current)

        return result
    }

    fun getNotIntersections(ranges: List<Range>, range: Range): List<Range> {
        if(ranges.isEmpty()) return listOf(range)

        val merged = mergeOverlappedRanges(ranges)

        val result = mutableListOf<Range>()

        if (range.from < merged.first().from) {
            result.add(Range(range.from, merged.first().from - 1))
        }

        if (merged.last().to < range.to) {
            result.add(Range(merged.last().to + 1, range.to))
        }

        merged.windowed(2).forEach {
            if (it.last().from - it.first().to > 1) {
                result.add(
                    Range(
                        it.first().to + 1,
                        it.last().from - 1
                    )
                )
            }
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val seeds = getSeeds(input.first())
            .chunked(2) {
                val (from, count) = it
                Range(from, from.plus(count - 1))
            }
        val mappings = getMappings(input.drop(2))

        val from = "seed"
        val to = "location"

        var current = from
        var currentRanges = mergeOverlappedRanges(seeds)

        while (current != to) {
            val mapping = mappings.first { it.from == current }
            current = mapping.to
            val newRanges = currentRanges.flatMap { range ->
                val parsedIntersections = mapping.values.mapNotNull {
                    val start = max(range.from, it.fromSrc)
                    val end = min(range.to, it.fromSrc.plus(it.length - 1))

                    if (start <= end)
                        Range(
                            start - it.fromSrc + it.fromDest,
                            end - it.fromSrc + it.fromDest
                        )
                    else
                        null
                }

                val normalIntersections = mapping.values.mapNotNull {
                    val start = max(range.from, it.fromSrc)
                    val end = min(range.to, it.fromSrc.plus(it.length - 1))

                    if (start <= end)
                        Range(
                            start,
                            end
                        )
                    else
                        null
                }

                val nonIntersections = getNotIntersections(normalIntersections, range)

                parsedIntersections + nonIntersections
            }

            currentRanges = mergeOverlappedRanges(newRanges)
        }

        return currentRanges.minOf { it.from }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class Mapping(
    val from: String,
    val to: String,
    val values: List<MapRange>
)

data class MapRange(
    val fromDest: Long,
    val fromSrc: Long,
    val length: Long
)

data class Range(
    val from: Long,
    val to: Long
)