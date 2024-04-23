package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDayScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetDayScheduleUseCase {
    override suspend fun invoke(yearMonth: String, day: String) : List<ScheduleModel> =
        scheduleRepository.selectDaySchedule(yearMonth, day).map { scheduleEntity ->
            scheduleEntity.toModel()
        }
}