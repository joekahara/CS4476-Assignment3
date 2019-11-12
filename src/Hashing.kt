import java.util.*
import kotlin.random.Random

const val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "

fun hash(str: String): String {
    val numericResult = mutableListOf(0,0,0,0,0) // initialize empty list for results

    val nums = str.toUpperCase().padEnd(if (str.length%5 == 0) 0 else (str.length+(5-str.length%5)), ' ').map { letters.indexOf(it) }
    if (!nums.all{ it in 0..26 }) return "Please enter a valid plaintext string (only alphabetic & space). "     // I/O error checking
    val matrices = nums.chunked(5).chunked(5)

    val round3Matrix = List(5) { MutableList(5) {0} }

    matrices.forEach { matrix -> // iterate through 25 character blocks
        matrix.forEachIndexed { chunkIndex, chunkOfFive -> // iterate through each row of the current 5x5 matrix

            // round 1
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num } // add each element of the matrix row to the proper result element

            // round 2
            if (chunkIndex < 4) Collections.rotate(chunkOfFive, -(chunkIndex+1)) // for all rows with index less than 4, rotate i times left
            chunkOfFive.mapIndexed { index, num -> numericResult[index] += num } // add to result similarly to round 1

            // round 3
            chunkOfFive.mapIndexed { index, num ->
                round3Matrix[index][chunkIndex] = num // copy values from original matrix to new rotated matrix so that rows are now columns
            }
        }

        // round 3 continued
        round3Matrix.forEachIndexed { chunkIndex, chunk ->
            if (chunkIndex < 4) Collections.rotate(chunk, chunkIndex+1) // rotate all rows with index less than 4 of new matrix i times left
            chunk.mapIndexed { index, num -> numericResult[index] += num } // add to result similarly to other steps
        }
    }

    return numericResult.map { letters[it % 27] }.joinToString("") // change result sums to characters, with modulo 27
}

fun mac(str: String, key: String): String {
    return hash(key + hash(key + str))
}

fun birthdayAttack(numOfVariations: Int): String {
    val testsAndResults = mutableMapOf<String,String>() // create empty mutable map to store hash inputs and outputs

    for (i in 0..numOfVariations) { // hash 'numOfVariations' random strings of 50 characters, store the input and output
        val randomString = List(20) { letters[Random.nextInt(0, letters.length)] }.joinToString("")
        testsAndResults[randomString] = hash(randomString)
    }

    val duplicateHashes = testsAndResults.values.groupingBy { it }.eachCount().filter { it.value > 1 } // check for and store duplicate hashes in the original map
    return testsAndResults.filterValues{ duplicateHashes.containsKey(it) }.toString() // return only the duplicate hashes and their inputs from the original map
}

fun main() {
    val plaintext = "the birthday attack can be performed for any hash functions including sha three"
    println("Plaintext: [$plaintext]")
    val result = hash(plaintext)
    println("Hash output: [$result]")
    val macResult = mac(plaintext, "abcde")
    println("MAC output: [$macResult]")
    println("Map of collisions found with 5000 variations {input=hash}: ${birthdayAttack(5000)}")
}