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
    @Query("SELECT * FROM category WHERE user_id = :userId")
    fun selectAll(userId: String) : Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT category_name FROM category WHERE category_name = :categoryName AND user_id = :userId")
    fun selectCategory(categoryName: String, userId: String) : String?

    @Transaction
    @Query(
        "SELECT category.category_name AS categoryName ,count(*) AS count, category.seq_no AS seqNo " +
        "FROM category " +
        "LEFT OUTER JOIN schedule " +
        "ON schedule.category_name = category.category_name " +
        "AND category.user_id = :userId " +
        "GROUP BY category.category_name"
    )
    fun selectGroupByCategory(userId: String) : Flow<List<CategoryGroupModel>>

    @Transaction
    @Query("UPDATE category set category_name = :categoryName WHERE seq_no = :seqNo")
    fun updateCategory(seqNo: Int, categoryName: String)

    @Delete
    fun delete(categoryEntity: CategoryEntity)
}