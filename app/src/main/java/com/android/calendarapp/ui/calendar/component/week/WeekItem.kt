package com.android.calendarapp.ui.calendar.component.week

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.calendarapp.ui.calendar.component.day.DayItem
import com.android.calendarapp.ui.calendar.model.DayItemModel

@Composable
fun WeekItem(
    weekData: List<DayItemModel>,
    weekIndex: Int,
    height: Dp,
    selectedDay: State<String>,
    dayItemOnclick: (String) -> Unit
) {
    Row(
        modifier = Modifier.height(height)
    ){
        for((index, dayData) in weekData.withIndex()) {

            DayItem(dayData, height, selectedDay, dayItemOnclick)

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

    val dayItemList: MutableList<DayItemModel> = mutableListOf()
    val borderDayItemList: MutableList<DayItemModel> = mutableListOf()

    for (i in 1..7) {
        dayItemList.add(
            DayItemModel(
                dayText = i.toString()
            )
        )
        borderDayItemList.add(
            DayItemModel(
                dayText = i.toString(),
                needBorder = true
            )
        )
    }

    Column {
        /*WeekItem(dayItemList, 0, 80.dp, MutableLiveData("18"), {})
        WeekItem(borderDayItemList, 0, 80.dp, MutableLiveData("18"), {})*/
    }


}