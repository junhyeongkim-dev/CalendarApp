package com.android.calendarapp.ui.common.dialog.models

import androidx.compose.runtime.State
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.output.IScheduleViewModelOutput

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
        val scheduleInput: IScheduleViewModelInput,
        val scheduleOutput: IScheduleViewModelOutput,
        val categoryItems: List<CategoryModel>,
        val onClickAddCategory: () -> Unit
    ) : DialogContent()
}