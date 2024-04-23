package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.data.entity.ScheduleGroupEntity
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun selectAllSchedule() : Flow<List<ScheduleEntity>>

    suspend fun selectDaySchedule(yearMonth: String, day: String) : List<ScheduleEntity>

    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    suspend fun selectGroupByYearMonth(scheduleYearMonth: String) : List<ScheduleGroupEntity>

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}