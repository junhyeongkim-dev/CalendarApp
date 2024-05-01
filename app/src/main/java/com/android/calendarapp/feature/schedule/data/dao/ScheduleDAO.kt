package com.android.calendarapp.feature.schedule.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheduleEntity: ScheduleEntity)

    @Transaction
    @Query("SELECT * FROM schedule WHERE user_id = :userId")
    fun selectAll(userId: String) : Flow<List<ScheduleEntity>>

    @Transaction
    @Query(
        "SELECT * " +
        "FROM schedule " +
        "WHERE schedule_year_month = :yearMonth AND schedule_day = :day AND user_id = :userId"
    )
    fun selectDaySchedule(yearMonth: String, day: String, userId: String) : Flow<List<ScheduleEntity>>

    @Transaction
    @Query(
        "SELECT " +
            "schedule_day AS day, " +
            "COUNT(schedule_day) AS count," +
            "schedule_year_month AS yearMonth " +
        "FROM schedule " +
        "WHERE schedule_year_month = :yearMonth " +
        "AND user_id = :userId " +
        "GROUP BY schedule_day"
    )
    fun selectGroupByYearMonth(yearMonth: String, userId: String): List<ScheduleGroupModel>

    @Transaction
    @Query(
        "UPDATE schedule " +
        "SET category_name = :changeCategoryName " +
        "WHERE category_name = :currentCategoryName " +
        "AND user_id = :userId"
    )
    fun updateCategory(currentCategoryName: String, changeCategoryName: String, userId: String)


    @Delete
    fun delete(scheduleEntity: ScheduleEntity)
}