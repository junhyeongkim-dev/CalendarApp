package com.android.calendarapp.feature.user.data.di

import com.android.calendarapp.feature.user.data.repository.UserRepository
import com.android.calendarapp.feature.user.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl) : UserRepository
}