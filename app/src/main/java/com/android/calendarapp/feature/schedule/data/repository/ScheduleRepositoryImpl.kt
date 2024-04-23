package com.android.calendarapp.feature.schedule.data.repository

import com.android.calendarapp.feature.schedule.data.dao.ScheduleDAO
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.data.entity.ScheduleGroupEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDAO: ScheduleDAO
) : ScheduleRepository {
    override suspend fun selectAllSchedule(): Flow<List<ScheduleEntity>> = scheduleDAO.selectAll()
    override suspend fun selectDaySchedule(
        yearMonth: String,
        day: String
    ): List<ScheduleEntity> = scheduleDAO.selectDaySchedule(yearMonth, day)

    override suspend fun insertSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.insert(scheduleEntity)

    override suspend fun selectGroupByYearMonth(scheduleYearMonth: String): List<ScheduleGroupEntity> =
        scheduleDAO.selectGroupByYearMonth(scheduleYearMonth)

    override suspend fun deleteSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.delete(scheduleEntity)

}