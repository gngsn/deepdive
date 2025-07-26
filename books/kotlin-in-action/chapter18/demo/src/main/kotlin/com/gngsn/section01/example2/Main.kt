package com.gngsn.section01.example2;

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        try {
            throw UnsupportedOperationException("Ouch!")
        } catch (u: UnsupportedOperationException) {
            println("Handled $u")
        }
    }
}

/** Output:
 *  Handled java.lang.UnsupportedOperationException: Ouch!
 */