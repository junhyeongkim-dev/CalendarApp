package com.android.calendarapp.feature.schedule.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "seq_no")
    val seqNo: Int,

    @ColumnInfo(name = "schedule_date")
    val scheduleDate: String,

    @ColumnInfo(name = "schedule_content")
    val scheduleContent: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String
)