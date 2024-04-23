package com.android.calendarapp.feature.category.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categoryEntity: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoryEntity: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM category")
    fun selectAll() : Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT category_name FROM category WHERE category_name = :categoryName")
    fun selectCategory(categoryName: String) : String?

    @Delete
    fun delete(categoryEntity: CategoryEntity)
}