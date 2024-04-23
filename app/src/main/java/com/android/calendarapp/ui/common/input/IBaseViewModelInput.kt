package com.android.calendarapp.ui.common.input

import com.android.calendarapp.ui.common.output.DialogState

interface IBaseViewModelInput {

    // 기본 다이얼로그 닫기
    fun onDismissDefaultDialog()

    // 기본 다이얼로그 열기
    fun showDialogDefault(dialogState: DialogState)

    // 스낵바 호출
    fun showSnackBar(message: String)
}