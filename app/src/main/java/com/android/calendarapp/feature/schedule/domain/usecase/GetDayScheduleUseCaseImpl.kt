package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDayScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetDayScheduleUseCase {
    override suspend fun invoke(yearMonth: String, day: String) : Flow<List<ScheduleModel>> =
        withContext(Dispatchers.IO) {
            scheduleRepository.selectDaySchedule(
                yearMonth = yearMonth,
                day = day,
                userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)
            ).map { scheduleEntityList ->
                scheduleEntityList.map { scheduleEntity ->
                    scheduleEntity.toModel()
                }
            }
        }
}