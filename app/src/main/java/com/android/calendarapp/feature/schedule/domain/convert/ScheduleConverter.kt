package com.android.calendarapp.feature.schedule.domain.convert

import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel

fun ScheduleEntity.toModel(): ScheduleModel = ScheduleModel(
    seqNo = this.seqNo,
    scheduleYearMonth = this.scheduleYearMonth,
    scheduleDay = this.scheduleDay,
    scheduleContent = this.scheduleContent,
    categoryName = this.categoryName
)

fun ScheduleModel.toEntity(): ScheduleEntity = ScheduleEntity(
    seqNo = this.seqNo,
    scheduleYearMonth = this.scheduleYearMonth,
    scheduleDay = this.scheduleDay,
    scheduleContent = this.scheduleContent,
    categoryName = this.categoryName
)