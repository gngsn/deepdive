package com.gngsn.section02.example2;

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    launch {
        try {
            while (true) {
                println("Heartbeat!")
                delay(500.milliseconds)
            }
        } catch (e: Exception) {
            println("Heartbeat terminated: $e")
            throw e
        }
    }
    launch {
        try {
            delay(1.seconds)
            throw UnsupportedOperationException("Ow!")
        } catch (u: UnsupportedOperationException) {
            println("Caught $u")
        }
    }
}

/**
 * Heartbeat!
 * Heartbeat!
 * Heartbeat!
 * Heartbeat terminated: kotlinx.coroutines.JobCancellationException: Parent job is Cancelling; job=BlockingCoroutine{Cancelling}@b065c63
 * Exception in thread "main" java.lang.UnsupportedOperationException: Ow!
 * 	at com.gngsn.section02.example1.MainKt$main$1$2.invokeSuspend(Main.kt:25)
 * 	...
 *
 * Process finished with exit code 1
 */