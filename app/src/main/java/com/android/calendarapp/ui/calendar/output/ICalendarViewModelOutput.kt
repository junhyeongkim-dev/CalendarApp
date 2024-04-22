package com.android.calendarapp.ui.calendar.output

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.calendar.model.DayItemModel
import com.android.calendarapp.ui.common.output.DialogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICalendarViewModelOutput {

    // 월별 일자 데이터 리스트
    val calendarData: Map<Int, List<List<DayItemModel>>>

    // 에러가 난 페이지 리스트
    val calendarErrorData: Map<Int, String>

    // 현재 선택된 일자
    @Stable
    val selectedDay: State<String>

    // 초기 데이터 완료 후 상태 전달 플로우
    val calendarUiState: StateFlow<CalendarUiEffect>

    // 달력 헤더
    val yearMonthHeader: State<String>
}

// 초기 데이터 저장 완료 Effect
sealed class CalendarUiEffect {
    data object Loading : CalendarUiEffect()
    data object Complete: CalendarUiEffect()
}
