package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentScheduleGroupUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetCurrentScheduleGroupUseCase {
    override suspend fun invoke(yearMonth: String): Flow<List<ScheduleGroupModel>> {

        return scheduleRepository.currentGroupByYearMonth(
            currentYearMonth = yearMonth,
            userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF))
    }
}