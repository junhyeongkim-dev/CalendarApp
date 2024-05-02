package com.android.calendarapp.ui.common.popup.config.output

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.SharedFlow

interface IConfigPopupOutput {
    val configPopupUiState: State<Boolean>
    val userNameEditText: State<String>
    val navigationState: SharedFlow<NavigateEffect>
}

sealed class NavigateEffect {
    data object GoConfigCategory : NavigateEffect()
    data object GoLogin : NavigateEffect()
}