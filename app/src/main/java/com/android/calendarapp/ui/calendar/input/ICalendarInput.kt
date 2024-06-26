package com.android.calendarapp.ui.calendar.input


interface ICalendarInput {

    // 달력 데이터 요청
    fun getMonthData(defaultPage: Int, page: Int)

    // 달력 헤더 년, 월 데이터 변경
    fun onChangeCalendarDate(date: String)

    // 일자 클릭 시 선택 보더 변경
    fun onClickDayItem(day: String)
}