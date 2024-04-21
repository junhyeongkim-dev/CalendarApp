package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow

interface GetCategoryListUseCase {
    suspend operator fun invoke() : Flow<List<CategoryModel>>
}