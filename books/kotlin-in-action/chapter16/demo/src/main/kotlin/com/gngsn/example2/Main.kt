package com.gngsn.example2;

import com.gngsn.example1.getRandomNumber
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun main() {
    val randomNumbers = flow {
        coroutineScope {
            repeat(10) {
                launch { emit(getRandomNumber()) }   // Error: emit can’t be called from a different coroutine
            }
        }
    }
}
