package com.android.calendarapp.ui.common.dialog.models

import androidx.compose.runtime.State
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput

sealed class DialogContent {
    data class Default(
        val text: String
    ) : DialogContent()
    data class Category(
        val text: State<String>,
        val onChangeText: (String) -> Unit,
        val isNotExistCategoryState: State<Boolean>
    ) : DialogContent()
    data class Schedule(
        val schedule: ScheduleModel,
        val scheduleInput: ISchedulePopupInput,
        val scheduleOutput: ISchedulePopupOutput,
        val categoryInput: ICategoryPopupInput,
        val categoryOutput: ICategoryPopupOutput,
    ) : DialogContent()

    data class UserName(
        val userName: State<String>,
        val onChangeUserName: (String) -> Unit
    ) : DialogContent()
}