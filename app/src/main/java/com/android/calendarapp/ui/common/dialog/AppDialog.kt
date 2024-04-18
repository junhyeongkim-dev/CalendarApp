package com.android.calendarapp.ui.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.calendarapp.ui.common.output.DialogState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DialogInit(uiState: StateFlow<DialogState>) {
    val dialogState by uiState.collectAsStateWithLifecycle()

    when(dialogState) {
        DialogState.Close -> {}
        is DialogState.Open -> {
            DialogWrapper(appDialog = (dialogState as DialogState.Open).dialogType)
        }
    }
}

sealed class AppDialog(
    open val title: String,
    open val content: String,
    open val confirmOnClick: () -> Unit,
    open val onDismiss: () -> Unit
) {
    data class DefaultOneButtonDialog(
        override val title: String,
        override val content: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit
    ) : AppDialog(title, content, confirmOnClick, onDismiss)

    data class DefaultTwoButtonDialog(
        override val title: String,
        override val content: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val cancelOnClick: () -> Unit
    ) : AppDialog(title, content, confirmOnClick, onDismiss)
}