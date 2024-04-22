package com.android.calendarapp.ui.common.popup

import androidx.compose.runtime.Composable
import com.android.calendarapp.ui.common.dialog.DialogInit
import com.android.calendarapp.ui.common.output.DialogState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CategoryDialogPopup(
    dialogState: StateFlow<DialogState>,
    onBackPress: () -> Unit
) {
    DialogInit(
        uiState = dialogState,
        onBackPress = onBackPress
    )
}