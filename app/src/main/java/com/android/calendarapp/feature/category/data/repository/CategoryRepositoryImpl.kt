package com.android.calendarapp.feature.category.data.repository

import com.android.calendarapp.feature.category.data.dao.CategoryDAO
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO
) : CategoryRepository {
    override suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>) = categoryDAO.insertAll(categoryEntityList)

    override suspend fun insertCategory(categoryEntity: CategoryEntity) = categoryDAO.insert(categoryEntity)

    override suspend fun selectCategoryList(): Flow<List<CategoryEntity>> = categoryDAO.selectAll()

    override suspend fun selectCategory(categoryName: String): String? = categoryDAO.selectCategory(categoryName)
    override suspend fun selectGroupByCategory(): Flow<List<CategoryGroupModel>> = categoryDAO.selectGroupByCategory()
    override suspend fun updateCategory(seqNo: Int, categoryName: String) = categoryDAO.updateCategory(seqNo, categoryName)

    override suspend fun deleteCategory(categoryEntity: CategoryEntity) = categoryDAO.delete(categoryEntity)
}