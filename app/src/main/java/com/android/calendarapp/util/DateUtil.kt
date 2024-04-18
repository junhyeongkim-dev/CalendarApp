package com.android.calendarapp.util

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
        fun getDayOfWeek(year: Int, month: Int, day: Int): String {

            val date = LocalDate.of(year, month, day)

            return date.dayOfWeek.name
        }

        // 월의 일 수 계산
        fun getLastDayOfMonth(year: Int, month: Int) : Int {

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
        fun getPreviousMonth(year: Int, month: Int, minusCount: Int): Pair<Int, Int> {

            val currentDate = LocalDate.of(year, month, 1)

            val previousMonthDate = currentDate.minusMonths(minusCount.toLong())

            return Pair(previousMonthDate.year, previousMonthDate.monthValue)
        }

        // 미래 달 조회
        fun getNextMonth(year: Int, month: Int, plusCount: Int): Pair<Int, Int> {

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

        fun paddingMonth(month: Int): String {
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

        // 년,월을 받아 페이지로 변환
        fun convertYearMonthToPage(defaultPage: Int, date: String) : Int {
            return defaultPage + getIntervalMonth(date)
        }
    }
}

const val dateDelimiter = "-"