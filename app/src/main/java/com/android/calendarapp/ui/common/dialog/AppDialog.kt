package com.android.calendarapp.ui.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DialogInit(
    uiState: StateFlow<DialogUiState>
) {
    val dialogState by uiState.collectAsStateWithLifecycle()

    when(dialogState) {
        is DialogUiState.Dismiss -> {

        }
        is DialogUiState.Show -> {
            DialogWrapper(appDialog = (dialogState as DialogUiState.Show).dialogType)
        }
    }
}

sealed class DialogUiState{
    data object Dismiss: DialogUiState()

    data class Show(
        val dialogType: AppDialog
    ) : DialogUiState()
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
        val cancelOnClick: () -> Unit,
        val text: State<String>,
        val isNotExistCategoryState: State<Boolean>,
        val onChangeText: (String) -> Unit
    ) : AppDialog(title, confirmOnClick, onDismiss)

    data class ScheduleDialog(
        override val title: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val cancelOnClick: () -> Unit,
        val schedule: ScheduleModel,
        val scheduleInput: ISchedulePopupInput,
        val scheduleOutput: ISchedulePopupOutput,
        val categoryInput: ICategoryPopupInput,
        val categoryOutput: ICategoryPopupOutput
    ) : AppDialog(title, confirmOnClick, onDismiss)

    data class UserNameDialog(
        override val title: String,
        override val confirmOnClick: () -> Unit,
        override val onDismiss: () -> Unit,
        val cancelOnClick: () -> Unit,
        val userName: State<String>,
        val onChangeUserName: (String) -> Unit
    ) : AppDialog(title, confirmOnClick, onDismiss)
}