package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
class GetCategoryGroupListUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : GetCategoryGroupListUseCase {
    override suspend fun invoke(): Flow<List<CategoryGroupModel>> =
        withContext(Dispatchers.IO) {
            categoryRepository.selectGroupByCategory()
        }
}

