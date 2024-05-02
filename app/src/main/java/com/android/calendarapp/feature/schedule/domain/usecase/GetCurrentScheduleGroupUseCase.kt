package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow

interface GetCurrentScheduleGroupUseCase {
    suspend operator fun invoke(yearMonth: String) : Flow<List<ScheduleGroupModel>>
}