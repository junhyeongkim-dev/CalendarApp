package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetScheduleGroupListUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetScheduleGroupListUseCase {
    override suspend fun invoke(scheduleYearMonth: String): List<ScheduleGroupModel> =
        withContext(Dispatchers.IO) {
            scheduleRepository.selectGroupByYearMonth(
                scheduleYearMonth = scheduleYearMonth,
                userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)
            )
        }
}