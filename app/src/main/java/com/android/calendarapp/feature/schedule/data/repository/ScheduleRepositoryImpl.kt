package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.dao.ScheduleDAO
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDAO: ScheduleDAO
) : ScheduleRepository {
    override suspend fun insertSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.insert(scheduleEntity)

    override suspend fun selectAllSchedule(userId: String): Flow<List<ScheduleEntity>> = scheduleDAO.selectAll(userId)

    override suspend fun selectDaySchedule(
        yearMonth: String,
        day: String,
        userId: String
    ): Flow<List<ScheduleEntity>> =
        scheduleDAO.selectDaySchedule(yearMonth, day, userId)

    override suspend fun selectGroupByYearMonth(
        scheduleYearMonth: String,
        userId: String
    ): List<ScheduleGroupModel> = scheduleDAO.selectGroupByYearMonth(scheduleYearMonth, userId)

    override suspend fun updateCategory(
        currentCategoryName: String,
        changeCategoryName: String,
        userId: String
    ) = scheduleDAO.updateCategory(currentCategoryName, changeCategoryName, userId)

    override suspend fun deleteSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.delete(scheduleEntity)
}