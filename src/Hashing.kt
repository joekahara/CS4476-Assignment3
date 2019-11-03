fun hash(str: String): String {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "
    var result = StringBuilder()
    val blocks = str.chunked(25)
    val matrices = blocks.map { it.chunked(5) }
    println(blocks.toString())
    println(matrices.toString())

    return result.toString()
}

fun main() {
    val plaintext = "this is the plaintext used for hashing in this assignment i am testing and wondering how many characters are in this string"
    val result = hash(plaintext)
}