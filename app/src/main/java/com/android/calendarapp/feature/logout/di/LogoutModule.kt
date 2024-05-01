package com.android.calendarapp.feature.logout.di

import com.android.calendarapp.feature.logout.usecase.LogoutUseCase
import com.android.calendarapp.feature.logout.usecase.LogoutUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LogoutModule {

    @Binds
    @Singleton
    abstract fun bindLogoutUseCase(logoutUseCaseImpl: LogoutUseCaseImpl) : LogoutUseCase
}