package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryGroupListUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : GetCategoryGroupListUseCase {
    override suspend fun invoke(): Flow<List<CategoryGroupModel>> =
        categoryRepository.selectGroupByCategory()
}