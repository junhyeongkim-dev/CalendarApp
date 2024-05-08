package com.android.calendarapp.ui.schedule.output

import androidx.paging.PagingData
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.feature.user.domain.model.UserModel
import kotlinx.coroutines.flow.StateFlow

interface IScheduleOutput {
    val schedulePagingData: StateFlow<PagingData<ScheduleModel>>
    val userInfo: StateFlow<UserModel>
    val categoryList: StateFlow<List<CategoryModel>>
    val selectedCategory: StateFlow<String>
}