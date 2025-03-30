/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc figure07_01.kt
 *  ❯ kotlin Figure07_01Kt.class
 * ====================================
 */

data class Address(
    val country: String?,
)

data class Company(
    val address: Address?
)

data class Person(
    val company: Company?
)

fun main() {
    val personNull = Person(Company(null))
    println(personNull.company!!.address!!.country)
}
/**
 * Exception in thread "main" java.lang.NullPointerException
 *         at Figure07_01Kt.main(figure07_01.kt:23)
 *         at Figure07_01Kt.main(figure07_01.kt)
 *         at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
 *         at java.base/java.lang.reflect.Method.invoke(Method.java:580)
 *         at org.jetbrains.kotlin.runner.AbstractRunner.run(runners.kt:70)
 *         at org.jetbrains.kotlin.runner.Main.run(Main.kt:183)
 *         at org.jetbrains.kotlin.runner.Main.main(Main.kt:193)
 */