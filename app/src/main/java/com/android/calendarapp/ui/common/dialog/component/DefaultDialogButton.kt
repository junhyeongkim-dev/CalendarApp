package com.android.calendarapp.ui.common.dialog.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R

@Composable
fun RowScope.DefaultDialogButton(
    text: String,
    buttonColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxHeight()
            .width(0.dp)
            .weight(1F),
        onClick = {
            onClick.invoke()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        shape = RectangleShape
    ) {
        Text(
            text = text,
            fontSize = dimensionResource(id = R.dimen.dimen_popup_button).value.sp,
            color = textColor
        )
    }
}