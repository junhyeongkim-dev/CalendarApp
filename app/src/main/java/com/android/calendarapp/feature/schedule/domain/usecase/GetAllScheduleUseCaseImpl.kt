package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetAllScheduleUseCase {
    override suspend fun invoke(): Flow<List<ScheduleModel>> =
        scheduleRepository.selectAllSchedule(
            userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)
        ).map { scheduleEntityList ->
            scheduleEntityList.map { scheduleEntity ->
                scheduleEntity.toModel()
            }
        }
}