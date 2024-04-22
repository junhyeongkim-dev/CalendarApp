package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun selectScheduleList() : Flow<List<ScheduleEntity>>

    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}