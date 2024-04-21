package com.android.calendarapp.ui.common.output

import com.android.calendarapp.ui.common.dialog.AppDialog
import kotlinx.coroutines.flow.StateFlow

interface IBaseViewModelOutput {
    val defaultDialogState: StateFlow<DialogState>
}

sealed class DialogState {
    data object Dismiss : DialogState()

    data class Show(
        val dialogType: AppDialog
    ) : DialogState()
}