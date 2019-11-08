import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

const val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "

fun hash(str: String): String {
    val numericResult = mutableListOf(0,0,0,0,0) // initialize empty list for results

    val nums = str.toUpperCase().padEnd(if (str.length%5 == 0) 0 else (str.length+(5-str.length%5)), ' ').map { letters.indexOf(it) }
    if (!nums.all{ it in 0..26 }) return "Please enter a valid plaintext string (only alphabetic & space). "     // I/O error checking
    val matrices = nums.chunked(5).chunked(5)

    val round3Matrix = List(5) { MutableList(5) {0} }

    matrices.forEach { matrix ->
        matrix.forEachIndexed { chunkIndex, chunkOfFive ->

            // round 1
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num }

            // round 2
            if (chunkIndex < 4) Collections.rotate(chunkOfFive, -(chunkIndex+1))
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num }

            // round 3
            chunkOfFive.mapIndexed { index, num ->
                round3Matrix[index][chunkIndex] = num
            }
        }

        // round 3 continued
        round3Matrix.forEachIndexed { chunkIndex, chunk ->
            if (chunkIndex < 4) Collections.rotate(chunk, chunkIndex+1)
            chunk.mapIndexed { index, num -> numericResult[index] += num }
        }
    }

    return numericResult.map { letters[it % 27] }.joinToString("") // change result sums to characters, with modulo 27
}

fun mac(str: String, key: String): String {
    return hash(key + hash(key + str))
}

fun birthdayAttack(digestLength: Int): String {
    val total = 2f.pow(digestLength/2f).roundToInt()
    val testsAndResults = mutableMapOf<String,String>()

    for (i in 0..total) {
        val randomString = List(50) { letters[Random.nextInt(0, letters.length)] }.joinToString("")
        testsAndResults[randomString] = hash(randomString)
    }

    for (i in 0..total) {
        val randomString = List(50) { letters[Random.nextInt(0, letters.length)] }.joinToString("")
        val randomHash = hash(randomString)
        if (testsAndResults.values.any { it == randomHash })
            return "Collision found with inputs [$randomString], ${testsAndResults.filterValues { it == randomHash }.keys} and output [$randomHash]"
    }

    return "Birthday attack unsuccessful with 2^(2/m) = $total variations used. "
}

fun main() {
    val plaintext = "the birthday attack can be performed for any hash functions including sha three"
    val result = hash(plaintext)
    println(result)
    val macResult = mac(plaintext, "abcde")
    println(macResult)
    println(birthdayAttack(5))
}