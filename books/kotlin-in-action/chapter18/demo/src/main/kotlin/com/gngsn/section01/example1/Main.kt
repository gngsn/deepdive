package com.gngsn.section01.example1;

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    try {
        launch {
            throw UnsupportedOperationException("Ouch!")
        }
    } catch (u: UnsupportedOperationException) {
        println("Handled $u")                     // 실행 안됨
    }
}

/** Output:
 *
 * Exception in thread "main" java.lang.UnsupportedOperationException: Ouch!
 * 	at com.gngsn.section01.example1.MainKt$main$1$1.invokeSuspend(Main.kt:9)
 * 	...
 */