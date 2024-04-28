package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import javax.inject.Inject

class UpdateScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : UpdateScheduleUseCase {

    override suspend fun invoke(currentCategoryName: String, changeCategoryName: String) {
        scheduleRepository.updateCategory(currentCategoryName, changeCategoryName)
    }
}