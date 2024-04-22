package com.android.calendarapp.ui.calendar.popup.output

import androidx.compose.runtime.State

interface IScheduleViewModelOutput {

    // 스케줄 추가 팝업 표시 상태
    val scheduleUiState: State<Boolean>

    // 스케줄 추가 텍스트필드
    val scheduleText: State<String>

    // 카테고리 드랍다운 표시 상태
    val dropDownState: State<Boolean>

    // 선택된 카테고리
    val selectedCategory: State<String>
}