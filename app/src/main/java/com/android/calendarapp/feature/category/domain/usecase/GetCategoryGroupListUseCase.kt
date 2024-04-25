package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.Flow

interface GetCategoryGroupListUseCase {
    suspend operator fun invoke() : Flow<List<CategoryGroupModel>>
}