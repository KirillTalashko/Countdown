package com.example.countdown.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ErrorManager {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var currentErrorJob: Job? = null

    fun postError(message: String, duration: Long = 3000) {
        currentErrorJob?.cancel()

        currentErrorJob = CoroutineScope(Dispatchers.Main).launch {
            _errorMessage.value = message

            if (duration > 0) {
                delay(duration)
                _errorMessage.value = null
            }
        }
    }
}