package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel

interface RemoveCategoryUseCase {
    suspend operator fun invoke(categoryGroupModel: CategoryGroupModel)
}