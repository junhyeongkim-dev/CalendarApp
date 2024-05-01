package com.android.calendarapp.ui.calendar.popup.input

import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface ISchedulePopupInput {

    // 달력의 일정 데이터 요청
    fun getMonthScheduleData(page: Int, date: String, isForce: Boolean)

    // 스케줄 입력창 노출여부 상태 변경
    fun onChangeScheduleState()

    // 일정 텍스트
    fun onChangeScheduleEditText(text: String)

    // 선택된 카테고리 변경
    fun onChangeCategory(category: String)

    // 스케줄 등록
    fun onClickAddSchedule(
        currentPage: Int,
        yearMonth: String,
        day: String,
        categoryName: String,
    )

    // 선택된 일자의 일정 조회
    fun setRefreshDayScheduleFlow(flow: Flow<Pair<String, String>>)

    // 일정 수정
    fun modifySchedule(
        seqNo: Int,
        categoryInput: ICategoryPopupInput,
        categoryOutput: ICategoryPopupOutput
    )

    // 다이얼로그 상태 전달을 위한 채널
    fun setDialogChannel(channel: Channel<DialogUiState>)

    fun deleteSchedule(
        seqNo: Int,
        currentPage: Int,
        yearMonth: String
    )
}