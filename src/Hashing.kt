fun hash(str: String): String {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
    val numericResult = mutableListOf(0,0,0,0,0) // initialize empty list

    val nums = str.toUpperCase().padEnd(str.length+(5-str.length%5), ' ').map { letters.indexOf(it) }
    if (!nums.all{ it in 0..26 }) return "Please enter a valid plaintext string (only alphabetic & space). "
    val matrices = nums.chunked(5).chunked(5)
    matrices.iterator().forEach { matrix ->
        matrix.iterator().forEach {
            for (i in 0 until 5)
                numericResult[i] += it[i]
        }
    }

    

    return numericResult.map { letters[it%27] }.toString()
}

fun main() {
    val plaintext = "abcdefghi jklmnopqrstuvw"
    val result = hash(plaintext)
    println(result)
}