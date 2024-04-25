package com.android.calendarapp.feature.user.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.calendarapp.feature.user.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE user_id = :userId")
    fun delete(userId: String)

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUser(userId: String) : Flow<UserEntity>
}