package com.gngsn.kcallable

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

fun foo(x: Int) = println(x)
class Person(val name: String, val age: Int)

fun main() {
    val kFunction = ::foo
    println(kFunction.name)        // foo
    kFunction.call(42)      // 42


    val person = Person("Alice", 29)
    val memberProperty = Person::age
    println(memberProperty.get(person))        // 29
}
