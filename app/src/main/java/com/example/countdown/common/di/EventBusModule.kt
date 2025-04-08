package com.example.countdown.common.di

import com.example.countdown.common.utils.EventDispatcher
import com.example.countdown.common.utils.TimerEventBusImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EventBusModule {
    @Binds
    fun bindEventDispatcher(impl: TimerEventBusImpl): EventDispatcher
}