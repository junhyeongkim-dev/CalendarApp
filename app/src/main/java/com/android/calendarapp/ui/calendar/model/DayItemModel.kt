package com.android.calendarapp.ui.calendar.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class DayItemModel(
    val dayText: String,
    var dayColor: Color = Color.Black,
    val needBorder: Boolean = false,
    val borderColor: Color = Color.Black,
    val borderBackground: Color = Color.Transparent,
    val isDayInCurrentMonth: Boolean = true
)
