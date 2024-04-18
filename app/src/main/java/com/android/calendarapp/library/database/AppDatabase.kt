package com.android.calendarapp.library.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.calendarapp.library.database.user.UserInfo
import com.android.calendarapp.library.database.user.UserInfoDAO

@Database(entities = [UserInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDAO() : UserInfoDAO
}