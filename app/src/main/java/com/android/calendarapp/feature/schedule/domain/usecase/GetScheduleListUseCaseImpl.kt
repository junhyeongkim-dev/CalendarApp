package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetScheduleListUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetScheduleListUseCase {
    override suspend fun invoke(): Flow<List<ScheduleModel>> =
        scheduleRepository.selectScheduleList().map { scheduleEntityList ->
            scheduleEntityList.map { scheduleEntity ->
                scheduleEntity.toModel()
            }
        }
}