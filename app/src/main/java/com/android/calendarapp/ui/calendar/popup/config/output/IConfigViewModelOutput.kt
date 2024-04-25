package com.android.calendarapp.ui.calendar.popup.config.output

import androidx.compose.runtime.State

interface IConfigViewModelOutput {
    val configPopupUiState: State<Boolean>
}