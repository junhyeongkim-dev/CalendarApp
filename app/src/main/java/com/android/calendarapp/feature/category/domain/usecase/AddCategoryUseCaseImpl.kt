package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.convert.toEntity
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : AddCategoryUseCase {
    override suspend fun invoke(categoryModel: CategoryModel) {
        categoryRepository.selectCategory(categoryModel.categoryName).also { categoryName ->

            if(categoryName == null) categoryRepository.insertCategory(categoryModel.toEntity())

        }
    }
}