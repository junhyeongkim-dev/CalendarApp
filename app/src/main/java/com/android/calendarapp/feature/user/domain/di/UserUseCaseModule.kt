package com.android.calendarapp.feature.user.domain.di

import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCaseImpl
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCaseImpl
import com.android.calendarapp.feature.user.domain.usecase.RemoveUserUseCase
import com.android.calendarapp.feature.user.domain.usecase.RemoveUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAddUserUseCase(addUserUseCaseImpl: AddUserUseCaseImpl) : AddUserUseCase

    @Binds
    @Singleton
    abstract fun bindGetUserUseCase(getUserUseCaseImpl: GetUserUseCaseImpl) : GetUserUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveUserUseCase(removeUserUseCaseImpl: RemoveUserUseCaseImpl) : RemoveUserUseCase
}