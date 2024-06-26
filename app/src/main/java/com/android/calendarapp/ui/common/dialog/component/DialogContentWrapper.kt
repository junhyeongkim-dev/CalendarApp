package com.android.calendarapp.ui.common.dialog.component

import androidx.compose.runtime.Composable
import com.android.calendarapp.ui.common.dialog.component.content.CategoryDialogContent
import com.android.calendarapp.ui.common.dialog.component.content.DefaultDialogContent
import com.android.calendarapp.ui.common.dialog.component.content.ScheduleDialogContent
import com.android.calendarapp.ui.common.dialog.component.content.UserNameDialogContent
import com.android.calendarapp.ui.common.dialog.models.DialogContent

@Composable
fun DialogContentWrapper(
    dialogContent: DialogContent
) {
    when(dialogContent) {
        is DialogContent.Default ->
            DefaultDialogContent(text = dialogContent.text)
        is DialogContent.Category ->
            CategoryDialogContent(
                text = dialogContent.text,
                isNotExistCategoryState = dialogContent.isNotExistCategoryState,
                onChangeText = dialogContent.onChangeText
            )

        is DialogContent.Schedule ->
            ScheduleDialogContent(
                scheduleModel = dialogContent.schedule,
                scheduleInput = dialogContent.scheduleInput,
                scheduleOutput = dialogContent.scheduleOutput,
                categoryInput = dialogContent.categoryInput,
                categoryOutput = dialogContent.categoryOutput,
            )

        is DialogContent.UserName ->
            UserNameDialogContent(
                userName = dialogContent.userName,
                onChangeUserName = dialogContent.onChangeUserName
            )
    }
}