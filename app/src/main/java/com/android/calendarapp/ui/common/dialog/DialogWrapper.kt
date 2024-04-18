package com.android.calendarapp.ui.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.android.calendarapp.ui.common.dialog.models.DialogButton
import com.android.calendarapp.ui.common.dialog.models.DialogContent

@Composable
fun DialogWrapper(appDialog: AppDialog) {
    when(appDialog) {
        is AppDialog.DefaultOneButtonDialog -> {
            DefaultOneButtonDialog(
                title = appDialog.title,
                contentText = appDialog.content,
                confirmOnClick = appDialog.confirmOnClick,
                onDismiss = appDialog.onDismiss

            )
        }
        is AppDialog.DefaultTwoButtonDialog -> {
            DefaultTwoButtonDialog(
                title = appDialog.title,
                contentText = appDialog.content,
                confirmOnClick = appDialog.confirmOnClick,
                cancelOnClick = appDialog.cancelOnClick,
                onDismiss = appDialog.onDismiss
            )
        }
    }
}


@Composable
fun DefaultOneButtonDialog(
    title: String,
    contentText: String,
    confirmOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Default(contentText),
        buttonList = listOf(
            DialogButton.Default(
                text = "확인",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun DefaultTwoButtonDialog(
    title: String,
    contentText: String,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Default(contentText),
        buttonList = listOf(
            DialogButton.Default(
                text = "취소",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = cancelOnClick
            ),
            DialogButton.Default(
                text = "확인",
                textColor = Color.Black,
                buttonColor = Color.Blue,
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}