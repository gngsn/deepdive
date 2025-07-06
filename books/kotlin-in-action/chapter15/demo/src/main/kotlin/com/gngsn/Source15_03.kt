import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    runBlocking {
        println(coroutineContext)
        launch {           // 일반 애플리케이션에서 사용하지 말 것
            println(coroutineContext)
            delay(1000.milliseconds)
            launch {
                delay(250.milliseconds)
                log("Grandchild done")
            }
            log("Child 1 done!")
        }
        GlobalScope.launch {
            delay(500.milliseconds)
            log("Child 2 done!")
        }
        log("Parent done!")
    }
}
