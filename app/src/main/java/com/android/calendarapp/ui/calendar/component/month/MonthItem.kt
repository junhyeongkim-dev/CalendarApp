package com.android.calendarapp.ui.calendar.component.month

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.calendarapp.ui.calendar.component.week.WeekItem
import com.android.calendarapp.ui.calendar.data.DayItemData
import com.android.calendarapp.ui.theme.CalendarAppTheme

@Composable
fun MonthItem(
    monthData: List<List<DayItemData>>
) {
    val height = 80.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for ((index, weekData) in monthData.withIndex()) {
            WeekItem(
                weekData = weekData,
                weekIndex = index,
                height = height
            )

            Spacer(
                modifier = Modifier
                    .height(0.5.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview
@Composable
fun MonthItemPreview() {
    val weekData: MutableList<DayItemData> = mutableListOf()
    val monthData: MutableList<List<DayItemData>> = mutableListOf()

    for(i in 1..28) {
        weekData.add(DayItemData(
            dayText = i.toString()
        ))

        if(i%7 == 0) {
            monthData.add(weekData.toList())
            weekData.clear()
        }
    }

    CalendarAppTheme {
        MonthItem(monthData)
    }
}