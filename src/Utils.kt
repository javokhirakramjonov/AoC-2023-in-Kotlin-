import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/input/$name.txt").readLines()

/**
 * Reads lines from the given input txt file.
 */
fun readInputAsArray(name: String) = Path("src/input/$name.txt").readLines().map{it.toCharArray().toTypedArray()}.toTypedArray()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/**
 * Rotate rectangle matrix 90 degree
 */
inline fun <reified T> rotate90Degree(arr: Array<Array<T>>) : Array<Array<T>> {
    val newArr = Array(arr.first().size) { Array(arr.size) { arr.first().first() } }

    arr.indices.forEach { x ->
        arr.first().indices.forEach { y ->
            newArr[newArr.size - 1 - y][x] = arr[x][y]
        }
    }

    return newArr
}

/**
 * Rotate rectangle matrix -90 degree
 */
inline fun <reified T> `rotate -90 Degree`(arr: Array<Array<T>>) : Array<Array<T>> {
    val newArr = Array(arr.first().size) { Array(arr.size) { arr.first().first() } }

    arr.indices.forEach { x ->
        arr.first().indices.forEach { y ->
            newArr[y][arr.size - 1 - x] = arr[x][y]
        }
    }

    return newArr
}