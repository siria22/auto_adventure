package com.example.data.common.di

import com.example.domain.usecase.nonfeature.log.GetLastLogIdUseCase
import com.example.domain.usecase.nonfeature.log.LogEventUseCase
import com.example.domain.utils.logging.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(
        logEventUseCase: LogEventUseCase,
        getLastLogIdUseCase: GetLastLogIdUseCase
    ): Logger {
        return Logger(logEventUseCase, getLastLogIdUseCase)
    }
}