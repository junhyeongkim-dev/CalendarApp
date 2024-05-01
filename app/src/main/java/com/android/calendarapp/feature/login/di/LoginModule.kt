package com.android.calendarapp.feature.login.di

import com.android.calendarapp.feature.login.usecase.GuestLoginUseCase
import com.android.calendarapp.feature.login.usecase.GuestLoginUseCaseImpl
import com.android.calendarapp.feature.login.usecase.NaverLoginUseCase
import com.android.calendarapp.feature.login.usecase.NaverLoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    @Singleton
    abstract fun bindNaverLoginUseCase(naverLoginUseCaseImpl: NaverLoginUseCaseImpl) : NaverLoginUseCase

    @Binds
    @Singleton
    abstract fun bindGuestLoginUseCase(guestLoginUseCaseImpl: GuestLoginUseCaseImpl) : GuestLoginUseCase
}