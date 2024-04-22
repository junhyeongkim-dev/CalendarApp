package com.android.calendarapp.feature.schedule.data.di

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleRepo(scheduleRepositoryImpl: ScheduleRepositoryImpl) : ScheduleRepository
}