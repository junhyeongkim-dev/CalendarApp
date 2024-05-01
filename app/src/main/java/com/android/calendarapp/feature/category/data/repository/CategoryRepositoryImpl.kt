package com.android.calendarapp.feature.category.data.repository

import com.android.calendarapp.feature.category.data.dao.CategoryDAO
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO
) : CategoryRepository {
    override suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>) =
        categoryDAO.insertAll(categoryEntityList)

    override suspend fun insertCategory(categoryEntity: CategoryEntity) =
        categoryDAO.insert(categoryEntity)

    override suspend fun selectCategoryList(userId: String): Flow<List<CategoryEntity>> =
        categoryDAO.selectAll(userId)

    override suspend fun selectCategory(categoryName: String, userId: String): String? =
        categoryDAO.selectCategory(
            categoryName = categoryName,
            userId = userId
        )

    override suspend fun selectGroupByCategory(userId: String): Flow<List<CategoryGroupModel>> =
        categoryDAO.selectGroupByCategory(userId)

    override suspend fun updateCategory(seqNo: Int, categoryName: String) =
        categoryDAO.updateCategory(
            seqNo = seqNo,
            categoryName = categoryName
        )

    override suspend fun deleteCategory(categoryEntity: CategoryEntity) =
        categoryDAO.delete(categoryEntity)
}