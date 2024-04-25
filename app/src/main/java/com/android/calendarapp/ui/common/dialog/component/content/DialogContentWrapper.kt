package com.android.calendarapp.ui.common.dialog.component.content

import androidx.compose.runtime.Composable
import com.android.calendarapp.ui.common.dialog.models.DialogContent

@Composable
fun DialogContentWrapper(
    dialogContent: DialogContent
) {
    when(dialogContent) {
        is DialogContent.Default -> DefaultDialogContent(text = dialogContent.text)
        is DialogContent.Category ->
            CategoryDialogContent(
                text = dialogContent.text,
                isNotExistCategoryState = dialogContent.isNotExistCategoryState,
                onChangeText = dialogContent.onChangeText
            )

        is DialogContent.Schedule -> {
            ScheduleDialogContent(
                scheduleModel = dialogContent.schedule,
                scheduleInput = dialogContent.scheduleInput,
                scheduleOutput = dialogContent.scheduleOutput,
                categoryItems = dialogContent.categoryItems,
                onClickAddCategory =dialogContent.onClickAddCategory
            )
        }
    }
}