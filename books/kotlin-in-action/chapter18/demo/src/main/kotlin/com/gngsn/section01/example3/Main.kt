package com.gngsn.section01.example3;

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val myDeferredInt: Deferred<Int> = async {
        throw UnsupportedOperationException("Ouch!")
    }
    try {
        val i: Int = myDeferredInt.await()
        println(i)
    } catch (u: UnsupportedOperationException) {
        println("Handled: $u")
    }
}

/** Output:
 *
 * Handled: java.lang.UnsupportedOperationException: Ouch!
 * Exception in thread "main" java.lang.UnsupportedOperationException: Ouch!
 * 	at com.gngsn.section01.example3.MainKt$main$1$myDeferredInt$1.invokeSuspend(Main.kt:9)
 * 	...
 *
 * Process finished with exit code 1
 */
