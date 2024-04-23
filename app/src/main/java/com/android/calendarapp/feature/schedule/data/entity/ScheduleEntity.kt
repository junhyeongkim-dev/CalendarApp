package com.android.calendarapp.feature.schedule.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "seq_no")
    val seqNo: Int = 0,

    @ColumnInfo(name = "schedule_year_month")
    val scheduleYearMonth: String,

    @ColumnInfo(name = "schedule_day")
    val scheduleDay: String,

    @ColumnInfo(name = "schedule_content")
    val scheduleContent: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String
)