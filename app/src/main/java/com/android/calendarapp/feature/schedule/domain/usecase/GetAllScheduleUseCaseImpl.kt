package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : GetAllScheduleUseCase {
    override suspend fun invoke(): Flow<List<ScheduleModel>> =
        scheduleRepository.selectAllSchedule().map { scheduleEntityList ->
            scheduleEntityList.map { scheduleEntity ->
                scheduleEntity.toModel()
            }
        }
}