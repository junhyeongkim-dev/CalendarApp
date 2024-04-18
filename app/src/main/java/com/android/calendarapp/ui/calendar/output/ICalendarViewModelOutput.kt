package com.android.calendarapp.ui.calendar.output

import androidx.lifecycle.LiveData
import com.android.calendarapp.ui.calendar.data.DayItemData
import kotlinx.coroutines.flow.StateFlow

interface ICalendarViewModelOutput {

    // 월별 일자 데이터 리스트
    val calendarData: Map<Int, List<List<DayItemData>>>

    // 에러가 난 페이지 리스트
    val calendarErrorData: Map<Int, String>

    // 초기 데이터 완료 후 상태 전달 플로우
    val calendarUiState: StateFlow<CalendarUiEffect>

    // 달력 헤더
    val headerLiveData: LiveData<String>
}

// 초기 데이터 저장 완료 Effect
sealed class CalendarUiEffect {
    data object Loading : CalendarUiEffect()
    data object Complete: CalendarUiEffect()
}