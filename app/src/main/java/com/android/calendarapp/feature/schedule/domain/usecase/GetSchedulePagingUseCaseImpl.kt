package com.android.calendarapp.feature.schedule.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import com.android.calendarapp.ui.schedule.condition.ScheduleCondition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSchedulePagingUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetSchedulePagingUseCase {
    override suspend fun invoke(condition: ScheduleCondition): Flow<PagingData<ScheduleModel>> =
        withContext(Dispatchers.IO) {
            scheduleRepository.selectPagingSchedule(
                userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF),
                categoryName = condition.categoryName.ifEmpty { null }
            ).map { pagingData ->
                pagingData.map {  scheduleEntity ->
                    scheduleEntity.toModel()
                }
            }
        }
}