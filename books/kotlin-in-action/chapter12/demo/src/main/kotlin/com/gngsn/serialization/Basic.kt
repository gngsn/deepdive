package com.gngsn.serialization

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.PROPERTY)
annotation class JsonName(val name: String)

class Coffee {
    @JsonName("alias") val name: String = ""
    val price: Int = 0
}

data class Tea(
    @JsonName("alias") val name: String,
    val price: Int
)

fun getPropNames(obj: Any):String {
    val kClass = obj::class as KClass<*>
    val properties = kClass.memberProperties

    return properties.joinToString(", ") { prop ->
        val jsonName = prop.findAnnotation<JsonName>()
        println("jsonName: $jsonName")
        jsonName?.name ?: prop.name
    }
}

fun main() {
    println(getPropNames(Coffee()))
    println(getPropNames(Tea("Americano", 6000)))
}