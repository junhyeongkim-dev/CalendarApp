package com.android.calendarapp.ui.common.popup.config.output

import androidx.compose.runtime.State

interface IConfigViewModelOutput {
    val configPopupUiState: State<Boolean>
}