package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.convert.toEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : RemoveCategoryUseCase {
    override suspend fun invoke(categoryGroupModel: CategoryGroupModel) =
        withContext(Dispatchers.IO) {
            categoryRepository.deleteCategory(categoryGroupModel.toEntity())
        }
}