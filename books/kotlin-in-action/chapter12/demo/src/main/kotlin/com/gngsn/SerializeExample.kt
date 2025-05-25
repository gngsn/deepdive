package com.gngsn

import com.gngsn.jkid.serialization.serialize
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.PROPERTY)
annotation class JsonName(val name: String)

class Coffee {
    @JsonName("alias") val name: String = ""
    val price: Int = 0
}

data class Coffee2(
    @JsonName("alias") val name: String,
    val price: Int
)

fun findNames(obj: Any) {
    val kClass = obj::class as KClass<*>
    val properties = kClass.memberProperties

    properties.forEach { prop ->
        val jsonName = prop.findAnnotation<JsonName>()
        val propName = jsonName?.name ?: prop.name
        println(propName)
    }
}

fun main() {
    val coffeeSerialize = serialize(Coffee())       // Coffee Serialize: {"name": "", "price": 0}
    val coffee2Serialize = serialize(Coffee2("Americano", 6000))        // Coffee2 Serialize: {"name": "Americano", "price": 6000}

    println("Coffee Serialize: $coffeeSerialize")
    println("Coffee2 Serialize: $coffee2Serialize")

    findNames(Coffee2("Americano", 6000))
    findNames(Coffee())
}