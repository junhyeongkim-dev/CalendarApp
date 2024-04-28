package com.android.calendarapp.feature.category.data.repository

import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>)

    suspend fun insertCategory(categoryEntity: CategoryEntity)

    suspend fun selectCategoryList() : Flow<List<CategoryEntity>>

    suspend fun selectCategory(categoryName: String) : String?

    suspend fun selectGroupByCategory(): Flow<List<CategoryGroupModel>>

    suspend fun updateCategory(seqNo: Int, categoryName: String)

    suspend fun deleteCategory(categoryEntity: CategoryEntity)
}