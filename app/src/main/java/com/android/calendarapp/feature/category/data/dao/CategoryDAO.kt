package com.android.calendarapp.feature.category.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
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

    @Transaction
    @Query(
        "SELECT category.category_name ,count(*) AS count " +
        "FROM category " +
        "LEFT OUTER JOIN schedule ON schedule.category_name = category.category_name " +
        "GROUP BY category.category_name"
    )
    fun selectGroupByCategory() : Flow<List<CategoryGroupModel>>

    @Delete
    fun delete(categoryEntity: CategoryEntity)
}