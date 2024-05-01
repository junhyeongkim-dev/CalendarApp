package com.android.calendarapp.feature.calendar.di

import com.android.calendarapp.feature.calendar.usecase.MakeMonthData
import com.android.calendarapp.feature.calendar.usecase.MakeMonthDataImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    @Binds
    @Singleton
    abstract fun bindMakeMonthData(makeMonthDataImpl: MakeMonthDataImpl) : MakeMonthData
}