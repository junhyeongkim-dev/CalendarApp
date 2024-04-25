package com.android.calendarapp.ui.common.output

import androidx.compose.material3.SnackbarHostState
import com.android.calendarapp.ui.common.dialog.DialogUiState
import kotlinx.coroutines.flow.StateFlow

interface IBaseViewModelOutput {
    val defaultDialogUiState: StateFlow<DialogUiState>
    val snackBarHostState: SnackbarHostState
}