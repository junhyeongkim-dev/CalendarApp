package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.convert.toModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCategoryListUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val prefStorageProvider: PrefStorageProvider
) : GetCategoryListUseCase {
    override suspend operator fun invoke() : Flow<List<CategoryModel>> =
        withContext(Dispatchers.IO) {
            categoryRepository.selectCategoryList(
                prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)
            ).map { categoryEntityList ->
                categoryEntityList.map { categoryEntity ->
                    categoryEntity.toModel()
                }
            }
        }
}