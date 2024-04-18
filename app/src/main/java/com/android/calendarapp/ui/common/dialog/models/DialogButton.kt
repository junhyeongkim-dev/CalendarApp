package com.android.calendarapp.ui.common.dialog.models

import androidx.compose.ui.graphics.Color

sealed class DialogButton(
    open val text: String,
    open val buttonColor: Color,
    open val textColor: Color,
    open val onClick: () -> Unit
) {
    data class Default(
        override val text: String,
        override val buttonColor: Color,
        override val textColor: Color,
        override val onClick: () -> Unit
    ) : DialogButton(
        text = text,
        buttonColor = buttonColor,
        textColor = textColor,
        onClick = onClick
    )
}