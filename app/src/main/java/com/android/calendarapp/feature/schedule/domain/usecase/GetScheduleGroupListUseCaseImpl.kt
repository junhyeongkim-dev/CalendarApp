package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import javax.inject.Inject

class GetScheduleGroupListUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetScheduleGroupListUseCase {
    override suspend fun invoke(scheduleYearMonth: String): List<ScheduleGroupModel> =
        scheduleRepository.selectGroupByYearMonth(scheduleYearMonth)
}