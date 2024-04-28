package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun selectAllSchedule() : Flow<List<ScheduleEntity>>

    suspend fun selectDaySchedule(yearMonth: String, day: String) : Flow<List<ScheduleEntity>>

    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    suspend fun selectGroupByYearMonth(scheduleYearMonth: String) : List<ScheduleGroupModel>

    suspend fun updateCategory(currentCategoryName: String, changeCategoryName: String)

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}