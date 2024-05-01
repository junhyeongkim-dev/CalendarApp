package com.android.calendarapp.feature.category.domain.usecase

import android.content.Context
import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import com.android.calendarapp.util.GsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCategoryListUseCaseImpl @Inject constructor(
    private val context: Context,
    private val categoryRepository: CategoryRepository
) : AddCategoryListUseCase {
    override suspend fun invoke() =
        withContext(Dispatchers.IO){
            categoryRepository.insertCategoryList(
                GsonUtil.readJsonData("prepare_category_info.json", context)
            )
        }
}