package com.android.calendarapp.ui.common.popup.config

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConfigPopup(
    expandState: Boolean
) {
    DropdownMenu(
        modifier = Modifier
            .padding(10.dp),
        expanded = expandState,
        onDismissRequest = {  }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "이름 바꾸기",
                    color = Color.Black
                )
            },
            onClick = {

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

            }
        )
    }
}