package com.android.calendarapp.ui.calendar.input


interface ICalendarViewModelInput {

    // 달력 데이터 요청
    fun getMonthData(defaultPage: Int, page: Int)

    // 달력 헤더 년, 월 데이터 변경
    fun onChangeCalendarDate(date: String)

    // 일자 클릭 시 선택 보더 변경
    fun onClickDayItem(day: String)

    // 스케줄 입력창 노출여부 상태 변경
    fun onChangeScheduleState()

    // 일정 텍스트
    fun onChangeScheduleEditText(text: String)

    // 카테고리 노출 여부 변경
    fun onChangeDropDownState()

    // 선택된 카테고리 변경
    fun onChangeCategory(category: String)

    // 카테고리 추가 클릭
    fun onClickAddCategory()

    fun onDismissCategoryDialog()
}