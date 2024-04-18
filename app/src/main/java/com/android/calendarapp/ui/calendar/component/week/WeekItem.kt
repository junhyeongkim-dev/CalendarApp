package com.android.calendarapp.ui.calendar.component.week

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.calendarapp.ui.calendar.component.day.DayItem
import com.android.calendarapp.ui.calendar.data.DayItemData

@Composable
fun WeekItem(
    weekData: List<DayItemData>,
    weekIndex: Int,
    height: Dp
) {
    Row(
        modifier = Modifier.height(height)
    ){
        for((index, dayData) in weekData.withIndex()) {

            // 여섯번째 주에 처음에는 시작 spacer를 준다.
            if(weekIndex == 5 && index == 0) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp)
                        .background(Color.Black)
                )
            }

            DayItem(dayData, height)

            if(index != weekData.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp)
                        .background(Color.LightGray)
                )
            }else{
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp)
                        .background(Color.Black)
                )
            }
        }
    }
}

@Preview
@Composable
fun WeekItemPreview() {

    val dayItemList: MutableList<DayItemData> = mutableListOf()
    val borderDayItemList: MutableList<DayItemData> = mutableListOf()

    for (i in 1..7) {
        dayItemList.add(
            DayItemData(
                dayText = i.toString()
            )
        )
        borderDayItemList.add(
            DayItemData(
                dayText = i.toString(),
                needBorder = true
            )
        )
    }

    Column {
        WeekItem(dayItemList, 0, 80.dp)
        WeekItem(borderDayItemList, 0, 80.dp)
    }


}