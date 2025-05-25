package com.gngsn

import com.gngsn.jkid.CustomSerializer
import com.gngsn.jkid.ValueSerializer
import com.gngsn.jkid.createInstance
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation

object DateSerializer : ValueSerializer<Date> {
    private val dateFormat = SimpleDateFormat("dd-mm-yyyy")

    override fun toJsonValue(value: Date): Any? =
        dateFormat.format(value)

    override fun fromJsonValue(jsonValue: Any?): Date =
        dateFormat.parse(jsonValue as String)
}

private data class Person2(
    val name: String,
    @CustomSerializer(DateSerializer::class) val birthDate: Date
)
