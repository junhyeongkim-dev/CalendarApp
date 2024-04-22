package com.android.calendarapp.ui.calendar.popup.input

interface IScheduleViewModelInput {

    // 스케줄 입력창 노출여부 상태 변경
    fun onChangeScheduleState()

    // 일정 텍스트
    fun onChangeScheduleEditText(text: String)

    // 카테고리 리스트 팝업 노출 여부 변경
    fun onChangeDropDownState()

    // 선택된 카테고리 변경
    fun onChangeCategory(category: String)
}