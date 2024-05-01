package com.android.calendarapp.feature.calendar.usecase

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.android.calendarapp.R
import com.android.calendarapp.ui.calendar.model.DayItemModel
import com.android.calendarapp.ui.calendar.type.DayOfWeekType
import com.android.calendarapp.util.DateUtil
import com.android.calendarapp.util.ResourceUtil
import com.android.calendarapp.util.dateDelimiter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MakeMonthDataImpl @Inject constructor() : MakeMonthData{
    override suspend fun invoke(
        context: Context,
        defaultPage: Int,
        page: Int,
        userBirth: String
    ): List<List<DayItemModel>> {
        return withContext(Dispatchers.Default) {
            val date = if(defaultPage == page){
                // 첫 페이지일 때 현재 년월

                DateUtil.getCurrentYearMonth()
            } else{
                // 해당 페이지에 맞는 년월

                DateUtil.convertPageToYearMonth(defaultPage, page)
            }

            val weekData: MutableList<DayItemModel> = mutableListOf()
            val monthData: MutableList<List<DayItemModel>> = mutableListOf()

            val requestYear = date.split(dateDelimiter)[0].toInt()
            val requestMonth = date.split(dateDelimiter)[1].toInt()

            // 현재 달의 데이터 요청 시 현재 일 표시를 위한 day 설정
            val toDay =
                if (DateUtil.getCurrentYearMonth() == date) DateUtil.getCurrentDay() else ""


            // 해당 년. 월의 날짜 수
            val monthsDayCount = DateUtil.getLastDayOfMonth(requestYear, requestMonth)

            // 시작 요일
            val firstDayOfWeek = DateUtil.getDayOfWeek(requestYear, requestMonth, 1)

            // 마지막 요일
            val lastDayOfWeek = DateUtil.getDayOfWeek(requestYear, requestMonth, monthsDayCount)

            // 이전 달 추가 일 수
            val previousCount = DayOfWeekType.entries.indexOf(DayOfWeekType.valueOf(firstDayOfWeek))

            // 다음 달 추가 일 수
            val nextCount = 6 - DayOfWeekType.entries.indexOf(DayOfWeekType.valueOf(lastDayOfWeek))

            // 달력에 표시될 총 일 수
            var count = 0

            // 이전 월 날짜 세팅
            val previousMonth = DateUtil.getPreviousMonth(requestYear, requestMonth, 1) // 전월의 년,월
            val lastDay =
                DateUtil.getLastDayOfMonth(previousMonth.first, previousMonth.second) // 전월의 마지막일
            for (i in 0 until previousCount) {

                // 이전 월의 날짜는 횟수가 진행될 수록 앞의 날짜기 때문에 계속 리스트의 첫부분에 추가
                weekData.add(0,
                    DayItemModel(
                        dayText = (lastDay - i).toString(),
                        dayColor = Color.LightGray,
                        isDayInCurrentMonth = false
                    )
                )

                count++
            }

            // 현재 월 날짜 세팅
            for (day in 1.. monthsDayCount) {
                weekData.add(
                    if (userBirth.isNotEmpty() && requestMonth == userBirth.split(dateDelimiter)[0].toInt()
                        && day == userBirth.split(dateDelimiter)[1].toInt()) {
                        // 유저의 생일일 때

                        DayItemModel(
                            dayText = day.toString(),
                            needBorder = true,
                            borderColor = Color.Green
                        )
                    } else if (toDay.isNotEmpty() && toDay.toInt() == day){
                        // 오늘

                        DayItemModel(
                            dayText = day.toString(),
                            dayColor = Color.White,
                            needBorder = true,
                            borderBackground = Color(ResourceUtil.getColor(context, R.color.calendar_header)),
                            borderColor = Color.Unspecified
                        )
                    } else {
                        DayItemModel(
                            dayText = day.toString()
                        )
                    }
                )

                count++

                // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                if(count % 7 == 0) {

                    if( !(previousCount > 0 && monthData.isEmpty()) && weekData[0].dayText != toDay ) {
                        // 첫 주에 이전달 데이터가 있는 경우, 당일 제외 하고 첫번째 Item은 일요일로 빨간색으로 표시

                        weekData[0].dayColor = Color.Red
                    }

                    // 당일이 아닌 마지막 Item은 토요일로 파란색으로 표시
                    if(weekData[weekData.lastIndex].dayText != toDay) weekData[weekData.lastIndex].dayColor = Color.Blue

                    // 1주일치 month 에 추가
                    monthData.add(weekData.toList())

                    // 기존 데이터 초기화
                    weekData.clear()
                }
            }

            // 다음 월 날짜 세팅
            for (day in 1 .. nextCount) {

                // 이전 월의 날짜는 횟수가 진행될 수록 앞의 날짜기 때문에 계속 리스트의 첫부분에 추가
                weekData.add(
                    DayItemModel(
                        dayText = day.toString(),
                        dayColor = Color.LightGray,
                        isDayInCurrentMonth = false
                    )
                )

                count++

                // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                if(count % 7 == 0) {

                    // 첫날은 일요일로 빨간색
                    if( weekData[0].dayText != toDay ) {
                        // 첫 주에 이전달 데이터가 있는 경우, 당일 제외 하고 첫번째 Item은 일요일로 빨간색으로 표시

                        weekData[0].dayColor = Color.Red
                    }

                    // 1주일치 month 에 추가
                    monthData.add(weekData.toList())

                    weekData.clear()
                }
            }

            // 무조건 6주(42일)를 맞춘다.
            if(count < 42) {
                for( addCount in 1 .. (42 - count) ) {
                    weekData.add(
                        DayItemModel(
                            dayText = (nextCount + addCount).toString(),
                            dayColor = Color.LightGray,
                            isDayInCurrentMonth = false
                        )
                    )

                    count++

                    // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                    if(count % 7 == 0) {

                        // 1주일치 month 에 추가
                        monthData.add(weekData.toList())

                        weekData.clear()
                    }
                }
            }

            monthData
        }
    }
}