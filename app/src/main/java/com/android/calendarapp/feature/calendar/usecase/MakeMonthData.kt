package com.android.calendarapp.feature.calendar.usecase

import android.content.Context
import com.android.calendarapp.ui.calendar.model.DayItemModel

interface MakeMonthData {
    suspend operator fun invoke(
        context: Context,
        defaultPage: Int,
        page: Int,
        userBirth: String
    ) : List<List<DayItemModel>>
}