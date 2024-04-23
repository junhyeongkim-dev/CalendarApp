package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow


interface GetScheduleGroupListUseCase {
    suspend operator fun invoke(scheduleYearMonth: String) : List<ScheduleGroupModel>
}