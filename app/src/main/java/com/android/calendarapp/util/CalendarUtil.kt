package com.android.calendarapp.util

import androidx.compose.ui.graphics.Color
import com.android.calendarapp.ui.calendar.data.DayItemData
import com.android.calendarapp.ui.calendar.type.DayOfWeekType
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit


class DateUtil {

    companion object {

        fun getCurrentYearMonth() : String {
            val currentDate = LocalDate.now()

            val year = currentDate.year
            val month = currentDate.monthValue.toString().padStart(2, '0')

            return "${year}${dateDelimiter}${month}"
        }

        // 현재 일 조회
        fun getCurrentDay() : String {
            val currentDate = LocalDate.now()

            return currentDate.dayOfMonth.toString()
        }

        // 년, 월, 일을 받아 해당 요일 조회
        private fun getDayOfWeek(year: Int, month: Int, day: Int): String {

            val date = LocalDate.of(year, month, day)

            return date.dayOfWeek.name
        }

        // 월의 일 수 계산
        private fun getLastDayOfMonth(year: Int, month: Int) : Int {

            // 일수가 30일인 달
            val thirty = listOf(4, 6, 9, 11)

            return if (month == 2) {
                // 2월
                if ((year% 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    29
                }else {
                    28
                }
            }else if(thirty.contains(month)) {
                30
            }else{
                31
            }
        }

        // 과거 달 조회
        private fun getPreviousMonth(year: Int, month: Int, minusCount: Int): Pair<Int, Int> {

            val currentDate = LocalDate.of(year, month, 1)

            val previousMonthDate = currentDate.minusMonths(minusCount.toLong())

            return Pair(previousMonthDate.year, previousMonthDate.monthValue)
        }

        // 미래 달 조회
        private fun getNextMonth(year: Int, month: Int, plusCount: Int): Pair<Int, Int> {

            val currentDate = LocalDate.of(year, month, 1)

            val nextMonthDate = currentDate.plusMonths(plusCount.toLong())

            return Pair(nextMonthDate.year, nextMonthDate.monthValue)
        }

        private fun getIntervalMonth(targetDate: String) : Int {

            //현재 년월이 기준
            val defaultDate = getCurrentYearMonth()

            val start = YearMonth.of(
                defaultDate.split(dateDelimiter)[0].toInt(),
                defaultDate.split(dateDelimiter)[1].toInt()
            )

            val end = YearMonth.of(
                targetDate.split(dateDelimiter)[0].toInt(),
                targetDate.split(dateDelimiter)[1].toInt()
            )

            // ChronoUnit.MONTHS.between() 메서드를 사용하여 두 날짜 간의 월 차이를 계산
            return ChronoUnit.MONTHS.between(start, end).toInt()
        }

        private fun paddingMonth(month: Int): String {
            return month.toString().padStart(2, '0')
        }

        // 초기 페이지 번호를 기준으로 요청받은 페이지의 번년,월로 변환
        fun convertPageToYearMonth(defaultPage: Int, targetPage: Int) : String {

            val currentYearMonth = getCurrentYearMonth().split(dateDelimiter)

            return if(defaultPage > targetPage) {
                // 페이지가 첫페이지보다 이전 페이지 (이전 달)

                //페이지 차이만큼 이전 달 조회
                val (preYear, preMonth) = getPreviousMonth(
                    year = currentYearMonth[0].toInt(),
                    month = currentYearMonth[1].toInt(),
                    minusCount = defaultPage - targetPage
                )

                "${preYear}${dateDelimiter}${paddingMonth(preMonth)}"
            }else{
                // 페이지가 첫페이지보다 이후 페이지 (다음 달)

                //페이지 차이만큼 다음 달 조회
                val (nextYear, nextMonth) = getNextMonth(
                    year = currentYearMonth[0].toInt(),
                    month = currentYearMonth[1].toInt(),
                    plusCount = targetPage - defaultPage
                )

                "${nextYear}${dateDelimiter}${paddingMonth(nextMonth)}"
            }
        }

        fun makeMonthData(defaultPage: Int, page: Int, userBirth: String) : List<List<DayItemData>> {
            val date = if(defaultPage == page){
                // 첫 페이지일 때 현재 년월

                getCurrentYearMonth()
            } else{
                // 해당 페이지에 맞는 년월

                convertPageToYearMonth(defaultPage, page)
            }

            val weekData: MutableList<DayItemData> = mutableListOf()
            val monthData: MutableList<List<DayItemData>> = mutableListOf()

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
            val lastDay = DateUtil.getLastDayOfMonth(previousMonth.first, previousMonth.second) // 전월의 마지막일
            for (i in 0 until previousCount) {

                // 이전 월의 날짜는 횟수가 진행될 수록 앞의 날짜기 때문에 계속 리스트의 첫부분에 추가
                weekData.add(0,
                    DayItemData(
                        dayText = (lastDay - i).toString(),
                        dayColor = Color.LightGray
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

                        DayItemData(
                            dayText = day.toString(),
                            needBorder = true,
                            borderColor = Color.Green
                        )
                    } else if (toDay.isNotEmpty() && toDay.toInt() == day){
                        // 오늘

                        DayItemData(
                            dayText = day.toString(),
                            dayColor = Color.White,
                            needBorder = true,
                            borderBackground = Color.Green,
                            borderColor = Color.Unspecified
                        )
                    } else {
                        DayItemData(
                            dayText = day.toString()
                        )
                    }
                )

                count++

                // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                if(count % 7 == 0) {

                    if( !(previousCount > 0 && monthData.isEmpty()) ) {
                        // 첫 주에 이전달 데이터가 있는 경우 제외 하고 첫번째 Item은 일요일로 빨간색으로 표시

                        weekData[0].dayColor = Color.Red
                    }

                    // 마지막 Item은 토요일로 파란색으로 표시
                    weekData[weekData.lastIndex].dayColor = Color.Blue

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
                    DayItemData(
                        dayText = day.toString(),
                        dayColor = Color.LightGray
                    )
                )

                count++

                // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                if(count % 7 == 0) {

                    // 첫날은 일요일로 빨간색
                    weekData[0].dayColor = Color.Red

                    // 1주일치 month 에 추가
                    monthData.add(weekData.toList())
                }
            }

            return monthData
        }
    }
}

const val dateDelimiter = "-"