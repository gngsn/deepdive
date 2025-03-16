/** ===================================
 * To run this file, you'll do
 *
 *  $ kotlinc buildString.kt
 *  $ kotlin buildStringKt.class
 * ====================================
 */

fun alphabet() = buildString {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}

val fibonacci = buildList {
    addAll(listOf(1, 1, 2))
    add(3)
    add(index = 0, element = 3)
}

val shouldAdd = true

val fruits = buildSet {
    add("Apple")
    if (shouldAdd) {
        addAll(listOf("Apple", "Banana", "Cherry"))
    }
}

val medals = buildMap<String, Int> {
    put("Gold", 1)
    putAll(listOf("Silver" to 2, "Bronze" to 3))
}

fun main() {
    println(alphabet())     // ABCDEFGHIJKLMNOPQRSTUVWXYZ
                            // Now I know the alphabet!
    println(fibonacci)      // [3, 1, 1, 2, 3]
    println(fruits)         // [Apple, Banana, Cherry]
    println(medals)         // {Gold=1, Silver=2, Bronze=3}

}