package com.android.calendarapp.ui.calendar.popup.schedule.output

import androidx.compose.runtime.State
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.common.dialog.DialogUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IScheduleViewModelOutput {

    // 월별 스케줄 일정 카운트 및 대표 일정 리스트
    val scheduleData: Map<Int, Flow<List<ScheduleGroupModel>>>

    // 스케줄 추가 팝업 표시 상태
    val scheduleUiState: State<Boolean>

    // 스케줄 추가 텍스트필드
    val scheduleText: State<String>

    // 선택된 카테고리
    val selectedCategory: State<String>

    // 선택일의 스케줄 데이터
    val scheduleList: Flow<List<ScheduleModel>>
}