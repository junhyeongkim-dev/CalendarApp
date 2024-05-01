package com.android.calendarapp.feature.category.domain.usecase

import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.convert.toEntity
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val prefStorageProvider: PrefStorageProvider
) : AddCategoryUseCase {
    override suspend fun invoke(categoryModel: CategoryModel) {
        withContext(Dispatchers.IO) {
            val userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)

            categoryRepository.selectCategory(
                categoryName = categoryModel.categoryName,
                userId = userId
            ).also { categoryName ->

                // 등록하려는 카테고리 네임 조회 후 없을 때 등록
                if(categoryName == null) categoryRepository.insertCategory(
                    categoryModel.toEntity(userId)
                )
            }
        }
    }
}