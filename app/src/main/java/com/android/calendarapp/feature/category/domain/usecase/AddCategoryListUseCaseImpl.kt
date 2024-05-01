package com.android.calendarapp.feature.category.domain.usecase

import android.content.Context
import com.android.calendarapp.feature.category.data.repository.CategoryRepository
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
            if(!prefStorageProvider.getBoolean(PrefStorageConstance.DEFAULT_CATEGORY_PREF)) {
                categoryRepository.insertCategoryList(
                    GsonUtil.readJsonData("prepare_category_info.json", context)
                ).also {
                    prefStorageProvider.setBoolean(PrefStorageConstance.DEFAULT_CATEGORY_PREF, true)
                }
            }
        }
}