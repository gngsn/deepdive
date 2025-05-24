package com.gngsn


fun main() {
    val anonymousObject = object {
        val x = 1
    }

    println(anonymousObject::class.simpleName) // null
    println(anonymousObject::class.qualifiedName) // null
}