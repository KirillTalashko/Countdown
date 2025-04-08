package com.example.countdown.common.di

import android.app.NotificationManager
import android.content.Context
import com.example.countdown.common.utils.ErrorManager
import com.example.countdown.common.utils.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager,
        errorManager: ErrorManager
    ): NotificationHelper {
        return NotificationHelper(context, notificationManager, errorManager)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}