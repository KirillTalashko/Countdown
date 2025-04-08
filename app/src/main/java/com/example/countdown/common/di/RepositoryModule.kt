package com.example.countdown.common.di

import com.example.countdown.data.TimerRepository
import com.example.countdown.data.TimerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: TimerRepositoryImpl): TimerRepository
}