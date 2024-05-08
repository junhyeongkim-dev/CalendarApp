package com.android.calendarapp.feature.schedule.domain.usecase

import androidx.paging.PagingData
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.schedule.condition.ScheduleCondition
import kotlinx.coroutines.flow.Flow

interface GetSchedulePagingUseCase {
    suspend operator fun invoke(condition: ScheduleCondition) : Flow<PagingData<ScheduleModel>>
}