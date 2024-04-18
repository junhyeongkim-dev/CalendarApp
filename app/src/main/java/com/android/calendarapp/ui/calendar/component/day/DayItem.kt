package com.android.calendarapp.ui.calendar.component.day

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.ui.calendar.data.DayItemData
import com.android.calendarapp.ui.theme.CalendarAppTheme

@Composable
fun RowScope.DayItem(
    data: DayItemData,
    height: Dp
) {
    Box(
        modifier = Modifier
            .height(height)
            .width(0.dp)
            .weight(1f)
            .padding(
                top = 2.dp,
                end = 2.dp
            ),
        contentAlignment = Alignment.TopEnd
    ){
        Row {
            if(data.needBorder) {
                OutLineCircleTextContent(
                    dayText = data.dayText,
                    dayColor = data.dayColor,
                    borderColor = data.borderColor,
                    borderBackground = data.borderBackground
                )
            }else {
                TextContent(
                    dayText = data.dayText,
                    dayColor = data.dayColor
                )
            }
        }
    }
}

@Composable
private fun TextContent(
    dayText: String,
    dayColor: Color,
) {
    Text(
        text = dayText,
        fontSize = dimensionResource(id = R.dimen.dimen_calendar_day_of_week_text).value.sp,
        color = dayColor,
        style = LocalTextStyle.current.copy(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        )
    )
}

@Composable
private fun OutLineCircleTextContent(
    dayText: String,
    dayColor: Color,
    borderColor: Color,
    borderBackground: Color
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                color = borderBackground,
                shape = CircleShape
            ) // 배경색 설정, 필요에 따라 수정 가능
            .border(
                border = BorderStroke(1.dp, borderColor), // 테두리 색상과 두께 설정
                shape = CircleShape // 동그란 모양 설정
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayText,
            fontSize = dimensionResource(id = R.dimen.dimen_calendar_day_of_week_text).value.sp,
            color = dayColor,
            style = LocalTextStyle.current.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Preview
@Composable
fun DayItemPreview() {
    CalendarAppTheme {
        Row(
            modifier = Modifier.background(Color.White)
        ) {
            val data = DayItemData(
                dayText = "1"
            )
            DayItem(data, 80.dp)

            val borderData = DayItemData(
                dayText = "1",
                needBorder = true
            )
            DayItem(borderData, 80.dp)
        }
    }
}