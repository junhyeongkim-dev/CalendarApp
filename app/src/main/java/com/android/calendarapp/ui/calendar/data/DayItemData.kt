package com.android.calendarapp.ui.calendar.data

import androidx.compose.ui.graphics.Color

data class DayItemData(
    val dayText: String,
    var dayColor: Color = Color.Black,
    var needBorder: Boolean = false,
    var borderColor: Color = Color.Black,
    var borderBackground: Color = Color.Transparent
)
