package com.android.calendarapp.feature.category.data.repository

import com.android.calendarapp.feature.category.data.dao.CategoryDAO
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO
) : CategoryRepository {
    override suspend fun selectCategoryList(): Flow<List<CategoryEntity>> {
        return categoryDAO.selectAll()
    }

    override suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>) {
        categoryDAO.insertAll(categoryEntityList)
    }

    override suspend fun insertCategory(categoryEntity: CategoryEntity) {
        categoryDAO.insert(categoryEntity)
    }

    override suspend fun deleteCategory(categoryName: String) {
        categoryDAO.delete(categoryName)
    }
}