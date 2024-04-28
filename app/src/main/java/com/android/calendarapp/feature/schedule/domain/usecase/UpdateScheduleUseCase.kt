package com.android.calendarapp.feature.schedule.domain.usecase

interface UpdateScheduleUseCase {
    suspend operator fun invoke(currentCategoryName: String, changeCategoryName: String)
}