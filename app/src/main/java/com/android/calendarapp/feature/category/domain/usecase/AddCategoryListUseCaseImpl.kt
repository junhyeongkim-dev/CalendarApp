package com.android.calendarapp.feature.category.domain.usecase

import android.content.Context
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.feature.category.domain.convert.toEntity
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import com.android.calendarapp.util.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCategoryListUseCaseImpl @Inject constructor(
    private val context: Context,
    private val prefStorageProvider: PrefStorageProvider,
    private val categoryRepository: CategoryRepository
) : AddCategoryListUseCase {
    override suspend fun invoke() =
        withContext(Dispatchers.IO){
            val userId = prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF)

            if(!prefStorageProvider.getBoolean(PrefStorageConstance.DEFAULT_CATEGORY_PREF + userId)) {
                // 최초 기본 카테고리값을 저장한적 없을 때


                categoryRepository.insertCategoryList(

                    // 최초 카테고리 값 저장
                    GsonUtil.readJsonData<CategoryModel>("prepare_category_info.json", context)
                        .map { categoryModel ->
                            categoryModel.toEntity(userId)
                        }
                ).also {
                    prefStorageProvider.setBoolean(PrefStorageConstance.DEFAULT_CATEGORY_PREF + userId, true)
                }
            }
        }
}