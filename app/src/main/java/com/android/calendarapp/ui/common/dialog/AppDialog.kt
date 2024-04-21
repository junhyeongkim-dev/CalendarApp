package com.android.calendarapp.ui.common.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.calendarapp.ui.common.output.DialogState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DialogInit(
    uiState: StateFlow<DialogState>,
    onBackPress: () -> Unit
) {
    val dialogState by uiState.collectAsStateWithLifecycle()
    val backHandleState: MutableState<Boolean> = remember { mutableStateOf(false) }

    BackHandler(enabled = backHandleState.value) {
        onBackPress()
    }

    when(dialogState) {
        DialogState.Dismiss -> {
            backHandleState.value = false
        }
        is DialogState.Show -> {
            backHandleState.value = true

            DialogWrapper(appDialog = (dialogState as DialogState.Show).dialogType)
        }
    }
}

sealed class AppDialog(
    open val title: String,
    open val confirmOnClick: () -> Unit,
    open val onDismiss: () -> Unit
) {
    data class DefaultOneButtonDialog(
        override val title: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val content: String
    ) : AppDialog(title, confirmOnClick, onDismiss)

    data class DefaultTwoButtonDialog(
        override val title: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val content: String,
        val cancelOnClick: () -> Unit
    ) : AppDialog(title, confirmOnClick, onDismiss)

    data class CategoryDialog(
        override val title: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val text: State<String>,
        val onChangeText: (String) -> Unit,
        val cancelOnClick: () -> Unit
    ) : AppDialog(title, confirmOnClick, onDismiss)
}