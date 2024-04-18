package com.android.calendarapp.ui.common.dialog.component.content

import androidx.compose.runtime.Composable
import com.android.calendarapp.ui.common.dialog.models.DialogContent

@Composable
fun DialogContentWrapper(
    dialogContent: DialogContent
) {
    when(dialogContent) {
        is DialogContent.Default -> DefaultDialogContent(dialogContent.text)
    }
}