package com.android.calendarapp.library.login.di

import com.android.calendarapp.library.login.naver.manager.INaverLoginManager
import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
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
    abstract fun bindNaverLoginManager(naverLoginManager: NaverLoginManager) : INaverLoginManager
}