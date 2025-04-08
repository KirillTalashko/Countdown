package com.example.countdown.common.service.util

interface TimerServiceController {
    fun startTimer(totalSeconds: Int)
    fun stopTimer()
    fun resetTimer()
}