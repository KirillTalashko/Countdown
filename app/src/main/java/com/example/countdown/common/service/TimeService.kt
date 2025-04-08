package com.example.countdown.common.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.countdown.R
import com.example.countdown.common.utils.ErrorManager
import com.example.countdown.common.utils.EventDispatcher
import com.example.countdown.common.utils.NotificationHelper
import com.example.countdown.common.utils.format
import com.example.countdown.data.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var repository: TimerRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var vibrator: Vibrator

    @Inject
    lateinit var errorManager: ErrorManager

    @Inject
    lateinit var eventDispatcher: EventDispatcher


    private var timerJob: Job? = null
    private var remainingTime = 0
    private var isTimerRunning = false

    companion object {
        const val ACTION_START = "START_TIMER"
        const val ACTION_STOP = "STOP_TIMER"
        const val ACTION_RESET = "RESET_TIMER"
        const val EXTRA_REMAINING_SECONDS = "EXTRA_REMAINING_SECONDS"
        const val ACTION_START_AFTER_REBOOT = "ACTION_START_AFTER_REBOOT"
        const val NOTIFICATION_ID = 333
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                ACTION_START -> {
                    val seconds = intent.getIntExtra(EXTRA_REMAINING_SECONDS, 0)
                    if (seconds > 0) {
                        startTimer(seconds)
                    } else {
                        errorManager.postError("Invalid timer duration: $seconds")
                    }
                }

                ACTION_START_AFTER_REBOOT -> {
                    CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                        try {
                            delay(3000)
                            val savedTime = withContext(Dispatchers.IO) {
                                repository.getTimerState() ?: 0
                            }
                            if (savedTime > 0) {
                                notificationHelper.showRebootNotification(savedTime)
                                startTimer(savedTime)
                            }
                        } catch (e: Exception) {
                            errorManager.postError("Reboot recovery failed: ${e.message}")
                            stopSelf()
                        }
                    }
                }

                ACTION_STOP -> stopTimer()
                ACTION_RESET -> resetTimer()
                else -> {}
            }
        }
        return START_STICKY
    }

    private fun startTimer(totalSeconds: Int) {
        try {
            if (isTimerRunning) {
                return
            }
            isTimerRunning = true
            remainingTime = totalSeconds
            startForeground(NOTIFICATION_ID, createNotification())

            timerJob = CoroutineScope(Dispatchers.Main).launch {
                var lastNotificationTime = totalSeconds
                while (remainingTime > 0) {
                    delay(1000)
                    remainingTime--
                    sendUpdate(remainingTime)
                    repository.saveTimerState(remainingTime)
                    if (remainingTime % 60 == 0 && remainingTime != lastNotificationTime) {
                        lastNotificationTime = remainingTime
                        notificationHelper.sendMinuteNotification(remainingTime)
                    }
                }
                onTimerComplete()
            }
        } catch (e: Exception) {
            errorManager.postError("Timer error: ${e.message}")
            stopSelf()
        } finally {
            isTimerRunning = false
        }
    }


    private suspend fun sendUpdate(seconds: Int) {
        eventDispatcher.emitTimerUpdate(seconds)
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        isTimerRunning = false
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveTimerState(remainingTime)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveTimerState(0)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun saveState() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveTimerState(remainingTime)
        }
    }

    private fun onTimerComplete() {
        playCompletionEffects()
        notificationHelper.sendCompletionNotification()
        stopSelf()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartIntent = Intent(this, TimerService::class.java).apply {
            action = ACTION_START
            putExtra(EXTRA_REMAINING_SECONDS, remainingTime)
        }
        startForegroundService(restartIntent)
        super.onTaskRemoved(rootIntent)
    }

    private fun playCompletionEffects() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibrationEffect = VibrationEffect.createWaveform(
                    longArrayOf(0, 500, 200, 500), -1
                )
                vibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 500, 200, 500), -1)
            }
        } catch (e: Exception) {
            errorManager.postError("Vibration error $e")
        }
    }

    private fun createNotification(): Notification {
        return notificationHelper.buildForegroundNotification(
            title = getString(R.string.notification_timer_running),
            content = remainingTime.format()
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        saveState()
        super.onLowMemory()
    }
}