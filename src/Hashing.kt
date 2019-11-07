import java.util.*

fun hash(str: String): String {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
    var numericResult = mutableListOf(0,0,0,0,0) // initialize empty list for results

    val nums = str.toUpperCase().padEnd(if (str.length%5 == 0) 0 else (str.length+(5-str.length%5)), ' ').map { letters.indexOf(it) }
    if (!nums.all{ it in 0..26 }) return "Please enter a valid plaintext string (only alphabetic & space). "     // I/O error checking
    val matrices = nums.chunked(5).chunked(5)
    matrices.forEach { matrix ->
        matrix.forEachIndexed { chunkIndex, chunkOfFive ->

            // round 1
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num }

            // round 2
            if (chunkIndex < 4) Collections.rotate(chunkOfFive, -chunkIndex-1)
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num }

            // round 3

        }
    }

    return numericResult.map { letters[it % 27] }.toString()
}

fun main() {
    val plaintext = "abcdefghi jklmnopqrstuvwx"
    val result = hash(plaintext)
    println(result)
}