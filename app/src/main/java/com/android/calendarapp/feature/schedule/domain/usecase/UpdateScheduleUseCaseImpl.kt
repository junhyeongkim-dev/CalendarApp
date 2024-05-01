package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : UpdateScheduleUseCase {

    override suspend fun invoke(currentCategoryName: String, changeCategoryName: String) =
        withContext(Dispatchers.IO) {
            scheduleRepository.updateCategory(currentCategoryName, changeCategoryName)
        }
}