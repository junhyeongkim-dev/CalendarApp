package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface GetScheduleListUseCase {
    suspend operator fun invoke() : Flow<List<ScheduleModel>>
}