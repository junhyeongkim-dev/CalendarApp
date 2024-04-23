package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface GetDayScheduleUseCase {
    suspend operator fun invoke(yearMonth: String, day: String) : List<ScheduleModel>
}