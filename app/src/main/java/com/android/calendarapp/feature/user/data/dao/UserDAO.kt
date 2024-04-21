package com.android.calendarapp.feature.user.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.calendarapp.feature.user.data.entity.UserEntity

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("delete from user where user_id = :userId")
    fun delete(userId: String)

    @Query("SELECT * FROM user where user_id = :userId")
    fun getUser(userId: String) : UserEntity
}