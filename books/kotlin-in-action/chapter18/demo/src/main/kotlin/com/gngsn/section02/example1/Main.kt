package com.gngsn.section02.example1;

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {
    // 첫번째 코루틴: Heartbeat 역할의 코루틴. 단순히 루프 돌면서 메시지 출력
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
    // 두번째 코루틴: 1초 후 예외를 던짐. 이때, 예외를 잡아내지는 않음
    launch {
        delay(1.seconds)
        throw UnsupportedOperationException("Ow!")
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