package com.android.calendarapp.ui.common.popup.config

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.android.calendarapp.ui.common.popup.config.input.IConfigPopupInput
import com.android.calendarapp.ui.common.popup.config.output.ConfigDialog

@Composable
fun ConfigPopup(
    expandState: Boolean,
    configInput: IConfigPopupInput,
    configCategoryOnClick: () -> Unit
) {
    DropdownMenu(
        expanded = expandState,
        onDismissRequest = configInput::onChangePopupUiState
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "이름 바꾸기",
                    color = Color.Black
                )
            },
            onClick = {
                configInput.onChangeConfigDialogUiState(
                    dialogType = ConfigDialog.UserName
                )
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "카테고리 관리",
                    color = Color.Black
                )
            },
            onClick = {
                configCategoryOnClick.invoke()
            }
        )
    }
}