package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetScheduleGroupListUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetScheduleGroupListUseCase {
    override suspend fun invoke(scheduleYearMonth: String): List<ScheduleGroupModel> =
        scheduleRepository.selectGroupByYearMonth(scheduleYearMonth).map { scheduleGroupEntity ->
            scheduleGroupEntity.toModel()
        }
}