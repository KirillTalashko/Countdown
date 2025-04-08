package com.example.countdown.common.service.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.countdown.common.service.TimerService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerServiceControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerServiceController {

    override fun startTimer(totalSeconds: Int) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.EXTRA_REMAINING_SECONDS, totalSeconds)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun stopTimer() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_STOP
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun resetTimer() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_RESET
        }
        ContextCompat.startForegroundService(context, intent)
    }
}