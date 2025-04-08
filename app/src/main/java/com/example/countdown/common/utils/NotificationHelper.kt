package com.example.countdown.common.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.countdown.R
import com.example.countdown.common.service.TimerService.Companion.NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationHelper @Inject constructor(
    @ApplicationContext val context: Context,
    private val notificationManager: NotificationManager,
    private val errorManager: ErrorManager
) {
    fun buildForegroundNotification(title: String, content: String): Notification {
        createTimerChannel()
        return NotificationCompat.Builder(context, TIMER_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_timer)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun buildCompletionNotification(): Notification {
        createCompletionChannel()
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        return NotificationCompat.Builder(context, COMPLETION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.timer_completed))
            .setContentText(context.getString(R.string.time_is_up))
            .setSmallIcon(R.drawable.ic_timer_complete)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(true)
            .build()
    }

    private fun buildAndShowNotification(
        title: String,
        content: String,
        notificationId: Int
    ) {
        try {
            createTimerChannel()

            val notification = buildForegroundNotification(title, content)
            notificationManager.notify(notificationId, notification)
        } catch (e: Exception) {
            errorManager.postError(context.getString(R.string.error_notification_failed))
        }
    }

    fun sendMinuteNotification(remainingTime: Int) {
        try {
            val minutesLeft = remainingTime / 60
            val notification = buildForegroundNotification(
                title = "Осталось $minutesLeft ${minutesLeft.minutesDeclension()} до завершения",
                content = context.getString(R.string.timer_running)
            )
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            errorManager.postError(context.getString(R.string.error_notification_failed))
        }
    }

    fun sendCompletionNotification() {
        try {
            notificationManager.notify(
                COMPLETION_NOTIFICATION_ID,
                buildCompletionNotification()
            )
        } catch (e: Exception) {
            errorManager.postError(context.getString(R.string.error_notification_failed))
        }
    }

    private fun createTimerChannel() {
        if (notificationManager.getNotificationChannel(TIMER_CHANNEL_ID) == null) {
            NotificationChannel(
                TIMER_CHANNEL_ID,
                context.getString(R.string.timer_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.timer_channel_description)
                enableVibration(false)
            }.also { notificationManager.createNotificationChannel(it) }
        }
    }

    private fun createCompletionChannel() {

        if (notificationManager.getNotificationChannel(COMPLETION_CHANNEL_ID) == null) {
            NotificationChannel(
                COMPLETION_CHANNEL_ID,
                context.getString(R.string.completion_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.completion_channel_description)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
            }.also { notificationManager.createNotificationChannel(it) }
        }

    }

    fun showRebootNotification(seconds: Int) {
        val minutes = seconds / 60
        buildAndShowNotification(
            title = "Таймер восстановлен",
            content = "Осталось $minutes минут",
            notificationId = RESTORE_NOTIFICATION_ID
        )
    }


    private fun isAirplaneMode(): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }


    companion object {
        const val TIMER_CHANNEL_ID = "timer_foreground_channel"
        const val COMPLETION_CHANNEL_ID = "timer_completion_channel"
        const val COMPLETION_NOTIFICATION_ID = 2
        const val RESTORE_NOTIFICATION_ID = 334
    }
}