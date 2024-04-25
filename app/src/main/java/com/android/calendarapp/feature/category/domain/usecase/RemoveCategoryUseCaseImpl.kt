package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.convert.toEntity
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.user.domain.model.UserModel
import javax.inject.Inject

class RemoveCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : RemoveCategoryUseCase {
    override suspend fun invoke(categoryModel: CategoryModel) {
        categoryRepository.deleteCategory(categoryModel.toEntity())
    }
}