package com.android.calendarapp.feature.schedule.domain.di

import com.android.calendarapp.feature.schedule.domain.usecase.AddScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.AddScheduleUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleGroupListUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleGroupListUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.GetAllScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetAllScheduleUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.GetDayScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetDayScheduleUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCaseImpl
import com.android.calendarapp.feature.schedule.domain.usecase.UpdateScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.UpdateScheduleUseCaseImpl
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
    abstract fun bindGetScheduleUseCase(getScheduleListUseCaseImpl: GetAllScheduleUseCaseImpl) : GetAllScheduleUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveScheduleUseCase(removeScheduleUseCaseImpl: RemoveScheduleUseCaseImpl) : RemoveScheduleUseCase

    @Binds
    @Singleton
    abstract fun bindGetScheduleGroupUseCase(getScheduleGroupListUseCaseImpl: GetScheduleGroupListUseCaseImpl) : GetScheduleGroupListUseCase

    @Binds
    @Singleton
    abstract fun bindGetDayScheduleUseCase(getDayScheduleUseCaseImpl: GetDayScheduleUseCaseImpl) : GetDayScheduleUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateScheduleUseCase(updateScheduleUseCaseImpl: UpdateScheduleUseCaseImpl) : UpdateScheduleUseCase
}