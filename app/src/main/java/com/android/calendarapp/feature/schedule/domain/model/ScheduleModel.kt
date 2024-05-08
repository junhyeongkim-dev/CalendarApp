package com.android.calendarapp.feature.schedule.domain.model

data class ScheduleModel(
    val seqNo: Int = 0,

    val yearMonth: String,

    val day: String,

    val content: String,

    val categoryName: String
)
