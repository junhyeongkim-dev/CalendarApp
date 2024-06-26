package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val categoryRepository: CategoryRepository,
    private val prefStorageProvider: PrefStorageProvider
) : AddScheduleUseCase {
    override suspend fun invoke(scheduleModel: ScheduleModel) {

        withContext(Dispatchers.IO) {

            val userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)

            // 데이터베이서에 존재하는지 확인하여 카테고리 이름 삽입
            categoryRepository.selectCategory(
                categoryName = scheduleModel.categoryName,
                userId = userId
            )?.let {
                scheduleRepository.insertSchedule(scheduleModel.toEntity(userId))
            } ?: scheduleRepository.insertSchedule(
                scheduleModel.copy(categoryName = "").toEntity(userId)
            )
        }
    }
}