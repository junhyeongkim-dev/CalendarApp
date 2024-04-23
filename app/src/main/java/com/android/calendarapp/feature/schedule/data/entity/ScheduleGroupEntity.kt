package com.android.calendarapp.feature.schedule.data.entity

import androidx.room.ColumnInfo
import androidx.room.Dao

@Dao
class ScheduleGroupEntity (

    @ColumnInfo(name = "count")
    val count: Int,

    @ColumnInfo(name = "schedule_year_month")
    val yearMonth: String,

    @ColumnInfo(name = "schedule_day")
    val day: String,
)