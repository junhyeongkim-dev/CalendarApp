package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : UpdateCategoryUseCase {

    override suspend fun invoke(categoryGroupModel: CategoryGroupModel) {
        categoryRepository.updateCategory(categoryGroupModel.seqNo, categoryGroupModel.categoryName)
    }
}