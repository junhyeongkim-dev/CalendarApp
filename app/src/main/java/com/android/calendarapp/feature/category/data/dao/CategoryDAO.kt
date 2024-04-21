package com.android.calendarapp.feature.category.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categoryEntity: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoryEntity: CategoryEntity)

    @Query("delete from category where category_name = :categoryName")
    fun delete(categoryName: String)

    @Query("select * from category")
    fun selectAll() : Flow<List<CategoryEntity>>

    @Query("select * from category")
    fun selectAll1() : List<CategoryEntity>
}