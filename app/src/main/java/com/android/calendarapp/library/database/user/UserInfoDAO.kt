package com.android.calendarapp.library.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userInfo: UserInfo)

    @Query("delete from userinfo where user_id = :userId")
    fun delete(userId: String)

    @Query("SELECT * FROM userinfo where user_id = :userId")
    fun getUserInfo(userId: String) : UserInfo

    @Query("SELECT * FROM userinfo")
    fun getUserAll() : List<UserInfo>
}