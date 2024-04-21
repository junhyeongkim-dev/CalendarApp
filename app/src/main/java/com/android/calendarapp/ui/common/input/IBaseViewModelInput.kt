package com.android.calendarapp.ui.common.input

import com.android.calendarapp.ui.common.output.DialogState

interface IBaseViewModelInput {

    fun isLogin(): Boolean
    fun onDismissDefaultDialog()

    fun showDialogDefault(dialogState: DialogState)
}