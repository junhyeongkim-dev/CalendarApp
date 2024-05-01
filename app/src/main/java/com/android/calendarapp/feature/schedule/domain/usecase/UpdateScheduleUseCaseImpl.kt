package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val prefStorageProvider: PrefStorageProvider
) : UpdateScheduleUseCase {

    override suspend fun invoke(currentCategoryName: String, changeCategoryName: String) =
        withContext(Dispatchers.IO) {
            scheduleRepository.updateCategory(
                currentCategoryName = currentCategoryName,
                changeCategoryName = changeCategoryName,
                userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)
            )
        }
}