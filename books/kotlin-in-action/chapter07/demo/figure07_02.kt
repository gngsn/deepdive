/** ===================================
 * To run this file, you'll do
 *
 * ❯ kotlinc figure07_02.kt
 * ❯ kotlin Figure07_02Kt.class
 * ====================================
 */
fun verify(input: String?) {
    if (input.isNullOrBlank()) {
        println("Please fill in the required fields")
    }
}

fun main() {
    verify("먀") // 예외 발생 X
    verify(null) // 예외 발생 X
}