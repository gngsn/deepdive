package com.gngsn.serialization

import com.gngsn.jkid.serialization.serialize

fun main() {
    val coffeeSerialize = serialize(Coffee())       // Coffee Serialize: {"name": "", "price": 0}
    val teaSerialize = serialize(Tea("Americano", 6000))        // Coffee2 Serialize: {"name": "Americano", "price": 6000}
}