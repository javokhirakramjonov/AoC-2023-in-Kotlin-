import java.awt.Color
import javax.sound.sampled.Line
import javax.swing.GroupLayout.Group
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun List<Line3D>.sortedByZ(): List<Line3D> {
        return sortedWith { l1, l2 ->
            if (l1.p1.z != l2.p1.z) l1.p1.z.compareTo(l2.p1.z)
            else l1.p2.z.compareTo(l2.p2.z)
        }
    }

    fun parseLines(input: List<String>): List<Line3D> {
        return input.map {
            val (p1, p2) = it
                .split("~")
                .map {
                    val (x, y, z) = it
                        .split(",")
                        .map(String::toInt)
                    Point3D(x, y, z)
                }

            if (p1.z > p2.z)
                p2 to p1
            else
                p1 to p2
        }.map {
            val type = when {
                it.first.x != it.second.x -> LineType.X_ALIGNED
                it.first.y != it.second.y -> LineType.Y_ALIGNED
                it.first.z != it.second.z -> LineType.Z_ALIGNED
                else -> LineType.POINT
            }

            Line3D(it.first, it.second, type)
        }.sortedByZ()
    }

    fun isOverlapping(range1: IntRange, range2: IntRange): Boolean {
        return range1.first in range2 ||
                range1.last in range2 ||
                range2.first in range1 ||
                range2.last in range1
    }

    fun isCrashing(line1: Line3D, line2: Line3D): Boolean {
        return isOverlapping(line1.p1.x..line1.p2.x, line2.p1.x..line2.p2.x) &&
                isOverlapping(line1.p1.y..line1.p2.y, line2.p1.y..line2.p2.y)
    }

    fun fallDownward(actualLines: List<Line3D>): List<Line3D> {
        val lines = actualLines.toTypedArray()

        lines.forEachIndexed { index, line ->
            val newMinZ = lines
                .filter { isCrashing(it, line) }
                .filter { it.p2.z < line.p1.z }
                .maxOfOrNull { it.p2.z + 1} ?: 1

            lines[index] = line.copy(
                p1 = line.p1.copy(
                    z = newMinZ
                ),
                p2 = line.p2.copy(
                    z = newMinZ + line.p2.z - line.p1.z
                )
            )
        }

        return lines.toList()
    }

    fun part1(input: List<String>): Int {
        val lines = parseLines(input)//.also { println(it.joinToString("\n")); println() }
        val fallenLines = fallDownward(lines).sortedByZ()//.also { println(it.joinToString("\n")); println() }
        val set = fallenLines.toSet()
        val temp = fallenLines.toMutableList()

        return fallenLines.indices.count {
            val actual = temp[it]

            temp.remove(actual)

            val newForm = fallDownward(temp).toSet()

            temp.add(it, actual)

            set == newForm
        }
    }

    fun part2(input: List<String>): Int {
        val lines = parseLines(input)//.also { println(it.joinToString("\n")); println() }
        val fallenLines = fallDownward(lines).sortedByZ()//.also { println(it.joinToString("\n")); println() }
        val temp = fallenLines.toMutableList()

        return fallenLines.indices.sumOf {
            val actual = temp[it]

            temp.remove(actual)

            val newForm = fallDownward(temp)

            val res = temp.indices.count { temp[it] != newForm[it] }

            temp.add(it, actual)

            res
        }
    }

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}

enum class LineType {
    X_ALIGNED,
    Y_ALIGNED,
    Z_ALIGNED,
    POINT
}

data class Line3D(
    val p1: Point3D,
    val p2: Point3D,
    val type: LineType
)