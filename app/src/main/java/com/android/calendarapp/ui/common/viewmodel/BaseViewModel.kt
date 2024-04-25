package com.android.calendarapp.ui.common.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.ui.common.input.IBaseViewModelInput
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.output.IBaseViewModelOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel(

) : ViewModel(), IBaseViewModelInput, IBaseViewModelOutput {

    protected var _defaultDialogUiState: MutableStateFlow<DialogUiState> = MutableStateFlow(DialogUiState.Dismiss)
    override val defaultDialogUiState: StateFlow<DialogUiState> = _defaultDialogUiState

    override val snackBarHostState: SnackbarHostState = SnackbarHostState()

    override fun onDismissDefaultDialog() {
        viewModelScope.launch {
            _defaultDialogUiState.emit(DialogUiState.Dismiss)
        }
    }

    override fun showDialogDefault(dialogUiState: DialogUiState) {
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