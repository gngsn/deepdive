/** ===================================
 * To run this file, you'll do
 *
 *  ❯ kotlinc figure08_01.kt
 *  ❯ kotlin Figure08_01Kt.class
 * ====================================
 */

/**
 * ❯ javap Person
 * public final class Person {
 *   public Person(java.lang.String, java.lang.Integer);
 *   public Person(java.lang.String, java.lang.Integer, int, kotlin.jvm.internal.DefaultConstructorMarker);
 *   public final java.lang.String getName();
 *   public final java.lang.Integer getAge();
 *   public final java.lang.Boolean isOlderThan(Person);
 *   ...
 * }
 */
data class Person(val name: String,
                  val age: Int? = null) {

    fun isOlderThan(other: Person): Boolean? {
        if (age == null || other.age == null)
            return null
        return age > other.age
    }
}

/**
 * ❯ javap Pure
 * public Pure(int); */
class Pure(val age: Int)

fun main() {
    println(Person("Sam", 35).isOlderThan(Person("Amy", 42)))   // false
    println(Person("Sam", 35).isOlderThan(Person("Jane")))      // null
}