package com.android.calendarapp.feature.category.data.repository

import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun selectCategoryList() : Flow<List<CategoryEntity>>

    suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>)

    suspend fun insertCategory(categoryEntity: CategoryEntity)

    suspend fun deleteCategory(categoryName: String)
}