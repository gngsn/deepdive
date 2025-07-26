package com.gngsn

private var zeroTime = System.currentTimeMillis()

fun log(message: Any?) =
    println(
        "${System.currentTimeMillis() - zeroTime} " +
                "[${Thread.currentThread().name}] $message"
    )
