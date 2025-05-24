package com.gngsn
import kotlin.reflect.full.*

class Person(val name: String, val age: Int)

fun main() {
    val person = Person("Alice", 29)
    val kClass = person::class
    println(kClass.simpleName)      // Person
    kClass.memberProperties.forEach { println(it.name) }
    // age
    // name
}
