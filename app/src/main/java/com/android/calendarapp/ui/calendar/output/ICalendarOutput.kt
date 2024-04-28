package com.android.calendarapp.ui.calendar.output

import androidx.compose.runtime.Stable
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.ui.calendar.model.DayItemModel
import com.android.calendarapp.ui.common.dialog.DialogUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ICalendarOutput {

    // 유저정보
    val userInfo: StateFlow<UserModel>

    // 월별 일자 데이터 리스트
    val calendarData: Map<Int, List<List<DayItemModel>>>

    // 에러가 난 페이지 리스트
    val calendarErrorData: Map<Int, String>

    // 초기 데이터 완료 후 상태 전달 플로우
    val calendarUiState: StateFlow<CalendarUiEffect>

    // 달력 헤더
    @Stable
    val selectedYearMonth: StateFlow<String>

    // 현재 선택된 일자
    @Stable
    val selectedDay: StateFlow<String>

    // 년월, 일 을 묶어서 해당일자의 일정 조히를 위한 flow
    val refreshDayScheduleFlow: Flow<Pair<String, String>>

    // 다른 뷰모델로 부터 다이얼로그 오픈 요청을 받기위한 채널
    val dialogChannel: Channel<DialogUiState>

    val configCategoryEffect: SharedFlow<CalendarNavigateEffect>
}

// 초기 데이터 저장 완료 Effect
sealed class CalendarUiEffect {
    data object Loading : CalendarUiEffect()
    data object Complete: CalendarUiEffect()
}

sealed class CalendarNavigateEffect {
    data object GoConfigCategory : CalendarNavigateEffect()
}