package com.android.calendarapp.feature.schedule.domain.convert

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel

fun ScheduleEntity.toModel(): ScheduleModel = ScheduleModel(
    seqNo = this.seqNo,
    yearMonth = this.scheduleYearMonth,
    day = this.scheduleDay,
    content = this.scheduleContent,
    categoryName = this.categoryName
)

fun ScheduleModel.toEntity(userId: String): ScheduleEntity = ScheduleEntity(
    seqNo = this.seqNo,
    scheduleYearMonth = this.yearMonth,
    scheduleDay = this.day,
    scheduleContent = this.content,
    categoryName = this.categoryName,
    userId = userId
)