package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.user.domain.model.UserModel

interface RemoveCategoryUseCase {
    suspend operator fun invoke(categoryModel: CategoryModel)
}