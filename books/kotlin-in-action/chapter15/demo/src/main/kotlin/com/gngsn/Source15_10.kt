import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds


fun main() {
    runBlocking {
        val myJob = launch {
            repeat(5) {
                doCpuHeavyWork()
                if (!isActive) return@launch
                ensureActive()
            }
        }
        delay(600.milliseconds)
        myJob.cancel()
    }
}
