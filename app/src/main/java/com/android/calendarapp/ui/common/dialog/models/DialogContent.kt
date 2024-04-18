package com.android.calendarapp.ui.common.dialog.models

sealed class DialogContent(
    open val text: String
) {
    data class Default(
        override val text: String
    ) : DialogContent(text)
}