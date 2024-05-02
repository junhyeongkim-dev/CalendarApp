package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    suspend fun selectAllSchedule(userId: String) : Flow<List<ScheduleEntity>>

    suspend fun selectDaySchedule(yearMonth: String, day: String, userId: String) : Flow<List<ScheduleEntity>>

    suspend fun currentGroupByYearMonth(currentYearMonth: String, userId: String) : Flow<List<ScheduleGroupModel>>

    suspend fun selectGroupByYearMonth(scheduleYearMonth: String, userId: String) : List<ScheduleGroupModel>

    suspend fun updateCategory(currentCategoryName: String, changeCategoryName: String, userId: String)

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}