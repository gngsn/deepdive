package com.gngsn.example4;

import com.gngsn.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.milliseconds

class RadioStation {
    private val _messageFlow = MutableSharedFlow<Int>()  // 새 가변 공유 플로우
    val messageFlow = _messageFlow.asSharedFlow()        // 전역 읽기 전용 공유 플로우

    fun beginBroadcasting(scope: CoroutineScope) {
        scope.launch {
            while (true) {
                delay(500.milliseconds)
                val number = Random.nextInt(0..10)
                log("Emitting $number!")
                _messageFlow.emit(number)                // 가변 공유 플로우에 값 배출
            }
        }
    }
}

//fun main() = runBlocking {
//    RadioStation().beginBroadcasting(this)    // runBlocking의 코루틴 스코프에서 코루틴 시작
//}

fun main(): Unit = runBlocking {
    val radioStation = RadioStation()
    radioStation.beginBroadcasting(this)
    delay(600.milliseconds)
    radioStation.messageFlow.collect {
        log("A collecting $it!")
    }
}
