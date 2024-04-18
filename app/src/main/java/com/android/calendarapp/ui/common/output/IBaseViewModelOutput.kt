package com.android.calendarapp.ui.common.output

import com.android.calendarapp.ui.common.dialog.AppDialog
import kotlinx.coroutines.flow.StateFlow

interface IBaseViewModelOutput {
    val dialogState: StateFlow<DialogState>
}

sealed class DialogState {
    data object Close : DialogState()

    data class Open(
        val dialogType: AppDialog
    ) : DialogState()
}