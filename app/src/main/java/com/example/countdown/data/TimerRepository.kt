package com.example.countdown.data

interface TimerRepository {
    suspend fun saveTimerState(seconds: Int)
    suspend fun getTimerState(): Int?
}