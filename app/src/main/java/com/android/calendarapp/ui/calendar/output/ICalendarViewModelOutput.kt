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

    // 카테고리 리스트
    @Stable
    val categoryList: Flow<List<CategoryModel>>

    // 초기 데이터 완료 후 상태 전달 플로우
    val calendarUiState: StateFlow<CalendarUiEffect>

    // 스케줄 추가 팝업 표시 상태
    val scheduleUiState: State<Boolean>

    // 달력 헤더
    val yearMonthHeader: State<String>

    // 스케줄 추가 텍스트필드
    val scheduleText: State<String>

    // 카테고리 드랍다운 표시 상태
    val dropDownState: State<Boolean>

    // 선택된 카테고리
    val selectedCategory: State<String>

    // 카테고리 추가 다이얼로그 노출 여부
    val categoryDialogState: StateFlow<DialogState>

    // 카테고리 다이얼로그 텍스트필드
    val categoryDialogText: State<String>
}

// 초기 데이터 저장 완료 Effect
sealed class CalendarUiEffect {
    data object Loading : CalendarUiEffect()
    data object Complete: CalendarUiEffect()
}
