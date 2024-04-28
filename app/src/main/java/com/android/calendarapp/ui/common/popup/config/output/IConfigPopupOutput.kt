package com.android.calendarapp.ui.common.popup.config.output

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

interface IConfigPopupOutput {
    val configPopupUiState: State<Boolean>
    val configDialogUiState: StateFlow<ConfigDialog>
    val userNameEditText: State<String>
}

sealed class ConfigDialog {
    data object Dismiss: ConfigDialog()
    data object UserName: ConfigDialog()
}