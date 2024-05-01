package com.android.calendarapp.library.login.naver.di

import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.login.naver.manager.NaverLoginManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NaverModule {

    @Binds
    @Singleton
    abstract fun bindNaverLoginManager(naverLoginManagerImpl: NaverLoginManagerImpl) : NaverLoginManager
}