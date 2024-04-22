package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel

interface RemoveScheduleUseCase {
    suspend operator fun invoke(scheduleModel: ScheduleModel)
}