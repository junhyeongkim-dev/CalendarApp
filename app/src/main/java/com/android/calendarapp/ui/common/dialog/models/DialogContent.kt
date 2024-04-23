package com.android.calendarapp.ui.common.dialog.models

import androidx.compose.runtime.State

sealed class DialogContent(
) {
    data class Default(
        val text: String
    ) : DialogContent()
    data class Category(
        val text: State<String>,
        val onChangeText: (String) -> Unit,
        val isNotExistCategoryState: State<Boolean>
    ) : DialogContent()
}