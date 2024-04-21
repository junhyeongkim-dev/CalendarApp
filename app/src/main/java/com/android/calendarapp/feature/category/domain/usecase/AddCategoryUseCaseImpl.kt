package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.data.repository.CategoryRepositoryImpl
import com.android.calendarapp.feature.category.domain.convert.toEntity
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val categoryRepositoryImpl: CategoryRepositoryImpl
) : AddCategoryUseCase {
    override suspend fun invoke(categoryModel: CategoryModel) {
        categoryRepositoryImpl.insertCategory(categoryModel.toEntity())
    }

}