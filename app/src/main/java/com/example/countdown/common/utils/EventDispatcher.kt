package com.example.countdown.common.utils

import kotlinx.coroutines.flow.Flow

interface EventDispatcher {
    suspend fun emitTimerUpdate(seconds: Int)
    val timerUpdates: Flow<Int>
}