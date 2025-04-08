package com.example.countdown.common.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.PowerManager
import android.os.Vibrator
import android.os.VibratorManager
import com.example.countdown.common.service.util.TimerServiceController
import com.example.countdown.common.service.util.TimerServiceControllerImpl
import com.example.countdown.common.utils.ErrorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "TimerPrefs",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun systemStateChecker(
        @ApplicationContext context: Context
    ): Boolean {
        return try {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            powerManager.isInteractive
        } catch (e: Exception) {
            false
        }
    }

    @Provides
    @Singleton
    fun provideVibrator(@ApplicationContext context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 and above, we use VibratorManager
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator
        } else {
            // For older Android versions
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    @Provides
    @Singleton
    fun provideErrorManager(): ErrorManager {
        return ErrorManager()
    }

    @Provides
    @Singleton
    fun provideTimerServiceController(
        @ApplicationContext context: Context
    ): TimerServiceController {
        return TimerServiceControllerImpl(context)
    }
}