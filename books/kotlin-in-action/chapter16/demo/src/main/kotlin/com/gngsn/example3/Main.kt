package com.gngsn.example3;

import com.gngsn.example1.getRandomNumber
import com.gngsn.log
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val randomNumbers = channelFlow {     // 새 채널 플로우 생성
    repeat(10) {
        launch {
            send(getRandomNumber())   // 여러 코루틴에서 send 호출 가능
        }
    }
}

fun main() = runBlocking {
    randomNumbers.collect {
        log(it)
    }
}

/**
 * 0 [main] -1427193408
 * 14 [main] -163708667
 * 14 [main] 1107689576
 * 14 [main] 792207625
 * 14 [main] -1684781812
 * 14 [main] -596632076
 * 14 [main] 1346018116
 * 14 [main] 995004709
 * 14 [main] 1402699301
 * 14 [main] 343399653
 */