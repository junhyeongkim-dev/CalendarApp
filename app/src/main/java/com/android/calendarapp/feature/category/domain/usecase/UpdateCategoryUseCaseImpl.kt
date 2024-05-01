package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : UpdateCategoryUseCase {

    override suspend fun invoke(categoryGroupModel: CategoryGroupModel) =
        withContext(Dispatchers.IO) {
            categoryRepository.updateCategory(categoryGroupModel.seqNo, categoryGroupModel.categoryName)
        }
}