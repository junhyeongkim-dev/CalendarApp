package com.android.calendarapp.feature.schedule.domain.model

import androidx.room.ColumnInfo

data class ScheduleModel(
    val seqNo: Int = 0,

    val scheduleYearMonth: String,

    val scheduleDay: String,

    val scheduleContent: String,

    val categoryName: String
)
