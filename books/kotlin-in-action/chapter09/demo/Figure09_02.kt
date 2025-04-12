/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc Figure09_02.kt
 * ====================================
 */

class NumberBox(var value: Int) {
    operator fun plus(other: NumberBox): NumberBox {
        return NumberBox(this.value + other.value)
    }

    operator fun plusAssign(other: NumberBox) {
        this.value += other.value
    }
}

/* Kotlin Compile Error
figure09_02.kt:22:9: error: ambiguity between assign operator candidates:
fun plus(other: NumberBox): NumberBox
fun plusAssign(other: NumberBox): Unit
    box += another
        ^^
*/
fun main() {
    var box = NumberBox(10)
    val another = NumberBox(5)
    box += another
}