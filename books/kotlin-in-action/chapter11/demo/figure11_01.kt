/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc figure11_01.kt & kotlin Figure11_01Kt.class
 * ====================================
 */

inline fun <reified T> isA(value: Any) = value is T
inline fun <reified T> reifiedIsA(value: Any) = value is T


fun main() {
    isA<String>("abc")   // true
    isA<String>(123)     // false

    val listOf = listOf("one", 2, "three")
    println(listOf.filterIsInstance<String>())
}