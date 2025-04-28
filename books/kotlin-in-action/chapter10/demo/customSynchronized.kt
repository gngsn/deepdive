import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

inline fun <T> synchronized(lock: Lock, action: () -> T): T {   // inline 함수를 호출하는 부분은
    lock.lock()                                                 // 해당 함수의 본문으로 치환
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

fun foo(l: Lock) {
    println("Before sync")
    synchronized(l) {
        println("Action")
    }
    println("After sync")
}

/*
❯ kotlinc customSynchronized.kt
❯ kotlin CustomSynchronizedKt.class
Before sync
Action
After sync
 */
fun main() {
    val l = ReentrantLock()
    foo(l)
}