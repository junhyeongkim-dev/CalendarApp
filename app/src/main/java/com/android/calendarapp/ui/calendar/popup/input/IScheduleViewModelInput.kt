package com.android.calendarapp.ui.calendar.popup.input

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.common.dialog.DialogUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface IScheduleViewModelInput {

    // 달력의 일정 데이터 요청
    fun getMonthScheduleData(page: Int, date: String, isForce: Boolean)

    // 스케줄 입력창 노출여부 상태 변경
    fun onChangeScheduleState()

    // 일정 텍스트
    fun onChangeScheduleEditText(text: String)

    // 카테고리 리스트 팝업 노출 여부 변경
    fun onChangeDropDownState()

    // 선택된 카테고리 변경
    fun onChangeCategory(category: String)

    // 스케줄 등록
    fun onClickAddSchedule(
        currentPage: Int,
        yearMonth: String,
        day: String,
        categoryName: String
    )

    // 선택된 일자의 일정 조회
    fun setRefreshDayScheduleFlow(flow: Flow<Pair<String, String>>)

    fun modifySchedule(
        seqNo: Int,
        categoryList: List<CategoryModel>,
        onClickAddCategory: () -> Unit
    )

    fun setDialogChannel(channel: Channel<DialogUiState>)

    fun deleteSchedule(seqNo: Int, currentPage: Int, yearMonth: String)
}