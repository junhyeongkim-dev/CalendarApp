package com.android.calendarapp.feature.schedule.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ScheduleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheduleEntity: ScheduleEntity)

    @Query("select * from schedule")
    fun selectAll() : Flow<List<ScheduleEntity>>

    @Update
    fun update(scheduleEntity: ScheduleEntity)

    @Delete
    fun delete(scheduleEntity: ScheduleEntity)


}

