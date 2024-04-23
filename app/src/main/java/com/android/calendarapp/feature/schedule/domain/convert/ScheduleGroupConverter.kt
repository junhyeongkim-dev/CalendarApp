package com.android.calendarapp.feature.schedule.domain.convert

import com.android.calendarapp.feature.schedule.data.entity.ScheduleGroupEntity
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel

fun ScheduleGroupEntity.toModel(): ScheduleGroupModel = ScheduleGroupModel(
    count = this.count,
    yearMonth = this.yearMonth,
    day = this.day
)

fun ScheduleGroupModel.toEntity(): ScheduleGroupEntity = ScheduleGroupEntity(
    count = this.count,
    yearMonth = this.yearMonth,
    day = this.day
)