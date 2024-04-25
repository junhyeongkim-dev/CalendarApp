package com.android.calendarapp.ui.common.dialog.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.calendarapp.ui.common.dialog.component.DefaultDialogButton
import com.android.calendarapp.ui.common.dialog.models.DialogButton

@Composable
fun DialogButtonWrapper(
    buttonList: List<DialogButton>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {

        for (dialogButton in buttonList) {
            when(dialogButton) {
                is DialogButton.Default ->
                    DefaultDialogButton(
                        text = dialogButton.text,
                        buttonColor = dialogButton.buttonColor,
                        textColor = dialogButton.textColor,
                        onClick = dialogButton.onClick
                    )
            }
        }
    }
}