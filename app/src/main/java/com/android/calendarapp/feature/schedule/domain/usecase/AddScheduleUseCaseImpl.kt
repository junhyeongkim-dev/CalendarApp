package com.android.calendarapp.feature.schedule.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.schedule.data.repository.ScheduleRepository
import com.android.calendarapp.feature.schedule.domain.convert.toEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import javax.inject.Inject

class AddScheduleUseCaseImpl @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val categoryRepository: CategoryRepository
) : AddScheduleUseCase {
    override suspend fun invoke(scheduleModel: ScheduleModel) {

        // 데이터베이서에 존재하는지 확인하여 카테고리 이름 삽입
        categoryRepository.selectCategory(scheduleModel.categoryName)?.let {
            scheduleRepository.insertSchedule(scheduleModel.toEntity())
        } ?: scheduleRepository.insertSchedule(
            scheduleModel.copy(categoryName = "").toEntity()
        )
    }
}