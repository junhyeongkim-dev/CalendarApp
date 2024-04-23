package com.android.calendarapp.ui.calendar.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.ui.theme.CalendarAppTheme
import com.android.calendarapp.util.dateDelimiter

@Composable
fun CalendarHeader(
    date: String,
    previousOnClick: () -> Unit,
    nextOnClick: () -> Unit
) {
    val headerDate = "${date.split(dateDelimiter)[0]}년 ${date.split(dateDelimiter)[1]}월"

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(colorResource(id = R.color.calendar_header).value)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .then(
                        remember {
                            Modifier.clickable { previousOnClick.invoke() }
                        }
                    ),
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "이전 월 Icon"
            )

            Text(
                modifier = Modifier
                    .padding(
                        start = 25.dp,
                        end = 25.dp
                    ),
                text = headerDate,
                fontSize = dimensionResource(id = R.dimen.dimen_calendar_month_text).value.sp,
                color = Color.White
            )

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .then(
                        remember {
                            Modifier.clickable { nextOnClick.invoke() }
                        }
                    ),
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "다음 월 Icon"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .background(Color(colorResource(id = R.color.calendar_header).value)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayOfWeekItem("일")
            DayOfWeekItem("월")
            DayOfWeekItem("화")
            DayOfWeekItem("수")
            DayOfWeekItem("목")
            DayOfWeekItem("금")
            DayOfWeekItem("토")
        }
    }
}

@Composable
private fun RowScope.DayOfWeekItem(
    text: String
) {
    Text(
        modifier = Modifier
            .width(0.dp)
            .weight(1f),
        text = text,
        fontSize = 12.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}


@Preview
@Composable
fun CalendarHeaderPreview() {
    CalendarAppTheme {
        CalendarHeader("2024-04", {}, {})
    }
}