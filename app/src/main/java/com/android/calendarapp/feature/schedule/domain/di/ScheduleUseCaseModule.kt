package com.android.calendarapp.feature.schedule.domain.di

import com.android.calendarapp.feature.schedule.domain.usecase.AddScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.AddScheduleUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleListUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleListUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAddScheduleUseCase(addScheduleUseCaseImpl: AddScheduleUseCaseImpl) : AddScheduleUseCase

    @Binds
    @Singleton
    abstract fun bindGetScheduleUseCase(getScheduleListUseCaseImpl: GetScheduleListUseCaseImpl) : GetScheduleListUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveScheduleUseCase(removeScheduleUseCaseImpl: RemoveScheduleUseCaseImpl) : RemoveScheduleUseCase
}