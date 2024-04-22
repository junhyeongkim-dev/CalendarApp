package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel

interface AddScheduleUseCase {
    suspend operator fun invoke(scheduleModel: ScheduleModel)
}