package com.android.calendarapp.ui.common.base.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.ui.common.base.input.IBaseInput
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.base.output.IBaseOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel(

) : ViewModel(), IBaseInput, IBaseOutput {

    private var _defaultDialogUiState: MutableStateFlow<DialogUiState> = MutableStateFlow(DialogUiState.Dismiss)
    override val defaultDialogUiState: StateFlow<DialogUiState> = _defaultDialogUiState

    override val snackBarHostState: SnackbarHostState = SnackbarHostState()

    override fun onDismissDialog() {
        viewModelScope.launch {
            _defaultDialogUiState.emit(DialogUiState.Dismiss)
        }
    }

    override fun showDialog(dialogUiState: DialogUiState) {
        viewModelScope.launch {
            _defaultDialogUiState.emit(dialogUiState)
        }
    }

    override fun showSnackBar(message: String) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }
}