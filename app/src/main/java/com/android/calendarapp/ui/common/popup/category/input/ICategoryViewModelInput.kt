package com.android.calendarapp.ui.common.popup.category.input

import com.android.calendarapp.ui.common.dialog.DialogUiState
import kotlinx.coroutines.channels.Channel

interface ICategoryViewModelInput {

    // 카테고리 리스트 팝업 노출 여부 변경
    fun onChangeCategoryUiState()

    // 카테고리 추가 클릭
    fun showCategoryDialog()

    fun setDialogChannel(channel: Channel<DialogUiState>)
}