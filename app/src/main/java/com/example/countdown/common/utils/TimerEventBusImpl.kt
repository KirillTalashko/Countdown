package com.example.countdown.common.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TimerEventBusImpl @Inject constructor() : EventDispatcher {
    private val _events = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val timerUpdates = _events.asSharedFlow()
        .distinctUntilChanged()
        .onEach { "EventBus Dispatching: ${it.format()}" }

    override suspend fun emitTimerUpdate(seconds: Int) {
        _events.emit(seconds)
    }
}