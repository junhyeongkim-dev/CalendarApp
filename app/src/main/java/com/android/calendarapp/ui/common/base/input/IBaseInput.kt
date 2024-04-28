package com.android.calendarapp.ui.common.base.input

import com.android.calendarapp.ui.common.dialog.DialogUiState

interface IBaseInput {

    // 기본 다이얼로그 닫기
    fun onDismissDialog()

    // 기본 다이얼로그 열기
    fun showDialog(dialogUiState: DialogUiState)

    // 스낵바 호출
    fun showSnackBar(message: String)
}