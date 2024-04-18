package com.android.calendarapp.ui.calendar.input

import com.android.calendarapp.ui.calendar.data.DayItemData

interface ICalendarViewModelInput {

    // 달력 데이터 요청
    fun getMonthData(defaultPage: Int, page: Int)

    // 달력 헤더 년, 월 데이터 변경
    fun changeCalendarDate(date: String)
}