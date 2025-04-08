package com.example.countdown.common.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import com.example.countdown.R
import com.example.countdown.common.service.TimerService
import com.example.countdown.common.service.util.TimerServiceController
import com.example.countdown.common.utils.ErrorManager
import com.example.countdown.common.utils.NotificationHelper
import com.example.countdown.data.TimerRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var repository: TimerRepository

    @Inject
    lateinit var timerServiceController: TimerServiceController

    @Inject
    lateinit var errorManager: ErrorManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isInteractive) {
            Handler(Looper.getMainLooper()).postDelayed({
                onReceive(context, intent)
            }, 5000)
            return
        }

        val serviceIntent = Intent(context, TimerService::class.java).apply {
            action = ACTION_START_AFTER_REBOOT
        }

        try {
            context.startForegroundService(serviceIntent)
        } catch (e: Exception) {
            errorManager.postError(context.getString(R.string.error_service_start))
        }

    }

    companion object {
        const val ACTION_START_AFTER_REBOOT = "ACTION_START_AFTER_REBOOT"
    }

}