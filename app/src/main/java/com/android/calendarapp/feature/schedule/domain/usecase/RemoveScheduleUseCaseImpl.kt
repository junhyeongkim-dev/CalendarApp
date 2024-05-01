package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : RemoveScheduleUseCase {
    override suspend fun invoke(scheduleModel: ScheduleModel) =
        withContext(Dispatchers.IO) {
            scheduleRepository.deleteSchedule(scheduleModel.toEntity(""))
        }
}