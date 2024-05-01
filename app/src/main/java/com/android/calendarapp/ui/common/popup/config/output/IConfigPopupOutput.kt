package com.android.calendarapp.ui.common.popup.config.output

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IConfigPopupOutput {
    val configPopupUiState: State<Boolean>
    val configDialogUiState: StateFlow<ConfigDialog>
    val userNameEditText: State<String>
    val navigationState: SharedFlow<NavigateEffect>
}

sealed class ConfigDialog {
    data object Dismiss: ConfigDialog()
    data object UserName: ConfigDialog()
}

sealed class NavigateEffect {
    data object GoConfigCategory : NavigateEffect()
    data object GoLogin : NavigateEffect()
}