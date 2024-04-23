package com.android.calendarapp.ui.common.output

import androidx.compose.material3.SnackbarHostState
import com.android.calendarapp.ui.common.dialog.AppDialog
import kotlinx.coroutines.flow.StateFlow

interface IBaseViewModelOutput {
    val defaultDialogState: StateFlow<DialogState>
    val snackBarHostState: SnackbarHostState
}

sealed class DialogState {
    data object Dismiss : DialogState()

    data class Show(
        val dialogType: AppDialog
    ) : DialogState()
}