package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryModel

interface AddCategoryUseCase {
    suspend operator fun invoke(categoryModel: CategoryModel)
}