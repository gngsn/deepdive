/** ===================================
 * To run this file, you can do...
 *
 *  $ kotlinc lambda.kt
 *  $ kotlin LambdaKt.class
 * ====================================
*/

data class Person(val name: String, val age: Int)

/**
 * CASE1. Object 선언
 */
fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun code3_defineObject() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    findTheOldest(people)
    // Person(name=Bob, age=31)
}


/**
 * CASE2. Lambda 선언
 */
fun code4_defineLambda() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxByOrNull { it.age })
    // Person(name=Bob, age=31)
}

fun code6_joinToString() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val names = people.joinToString(
        separator = " ",
        transform = { p: Person -> p.name }
    )
    println(names)
    // Alice Bob
}

fun code7_joinToString_lambda() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val names = people.joinToString(" ") { p: Person -> p.name }
    println(names)
    // Alice Bob
}


fun main() {
    code3_defineObject()
    code4_defineLambda()
    code6_joinToString()
    code7_joinToString_lambda()
}
