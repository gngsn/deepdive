/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc figure08_04.kt & kotlin Figure08_04Kt.class
 * ====================================
 */
fun main() {
    println("trUE".toBoolean())
    // true
    println("7".toBoolean())
    // false
    println(null.toBoolean())
    // false
    println("true".toBooleanStrict())
    // true
    println("trUE".toBooleanStrict())
    // Exception in thread "main" java.lang.IllegalArgumentException: The string doesn't represent a boolean value: trUE
    //        at kotlin.text.StringsKt__StringsKt.toBooleanStrict(Strings.kt:1542)
    //        at Figure08_04Kt.main(figure08_04.kt:15)
    //        at Figure08_04Kt.main(figure08_04.kt)
    //        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
    //        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
    //        at org.jetbrains.kotlin.runner.AbstractRunner.run(runners.kt:70)
    //        at org.jetbrains.kotlin.runner.Main.run(Main.kt:183)
    //        at org.jetbrains.kotlin.runner.Main.main(Main.kt:193)
}