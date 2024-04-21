package com.android.calendarapp.ui.calendar.component.month

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.calendarapp.ui.calendar.component.week.WeekItem
import com.android.calendarapp.ui.calendar.model.DayItemModel
import com.android.calendarapp.ui.theme.CalendarAppTheme

@Composable
fun MonthItem(
    monthData: List<List<DayItemModel>>,
    selectedDay: State<String>,
    dayItemOnclick: (String) -> Unit
) {
    val height = 70.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for ((index, weekData) in monthData.withIndex()) {
            WeekItem(
                weekData = weekData,
                weekIndex = index,
                height = height,
                selectedDay = selectedDay,
                dayItemOnclick = dayItemOnclick
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
    val weekData: MutableList<DayItemModel> = mutableListOf()
    val monthData: MutableList<List<DayItemModel>> = mutableListOf()

    for(i in 1..28) {
        weekData.add(DayItemModel(
            dayText = i.toString()
        ))

        if(i%7 == 0) {
            monthData.add(weekData.toList())
            weekData.clear()
        }
    }

    CalendarAppTheme {
//        MonthItem(monthData, MutableLiveData("18"), {})
    }
}