package com.example.countdown.data

import android.content.SharedPreferences
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences
) : TimerRepository {

    override suspend fun saveTimerState(seconds: Int) {
        prefs.edit().putInt("remaining_time", seconds).apply()
    }

    override suspend fun getTimerState(): Int? {
        val savedTime = prefs.getInt("remaining_time", -1)
        return if (savedTime >= 0) savedTime else null
    }
}