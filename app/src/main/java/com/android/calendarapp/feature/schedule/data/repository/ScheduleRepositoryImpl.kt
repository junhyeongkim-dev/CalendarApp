package com.android.calendarapp.feature.schedule.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.calendarapp.feature.schedule.data.dao.ScheduleDAO
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDAO: ScheduleDAO
) : ScheduleRepository {

    companion object {
        const val PAGING_SIZE = 20
    }

    override suspend fun insertSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.insert(scheduleEntity)

    override suspend fun selectPagingSchedule(
        userId: String,
        categoryName: String?
    ): Flow<PagingData<ScheduleEntity>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = PAGING_SIZE*2,
                pageSize = PAGING_SIZE, // 페이지 당 로드할 아이템 수
                prefetchDistance = PAGING_SIZE*3,
                enablePlaceholders = false // 플레이스홀더 사용 여부
            ),
            pagingSourceFactory = {
                scheduleDAO.selectAll(
                    userId = userId,
                    categoryName = categoryName
                )
            }
        ).flow

    override suspend fun selectDaySchedule(
        yearMonth: String,
        day: String,
        userId: String
    ): Flow<List<ScheduleEntity>> =
        scheduleDAO.selectDaySchedule(yearMonth, day, userId)

    override suspend fun currentGroupByYearMonth(
        currentYearMonth: String,
        userId: String
    ): Flow<List<ScheduleGroupModel>> {
        return scheduleDAO.selectCurrent(currentYearMonth, userId)
    }

    override suspend fun selectGroupByYearMonth(
        scheduleYearMonth: String,
        userId: String
    ): List<ScheduleGroupModel> = scheduleDAO.selectGroupByYearMonth(scheduleYearMonth, userId)

    override suspend fun updateCategory(
        currentCategoryName: String,
        changeCategoryName: String,
        userId: String
    ) = scheduleDAO.updateCategory(currentCategoryName, changeCategoryName, userId)

    override suspend fun deleteSchedule(scheduleEntity: ScheduleEntity) = scheduleDAO.delete(scheduleEntity)
}