package com.android.calendarapp.library.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.calendarapp.feature.category.data.dao.CategoryDAO
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.schedule.data.dao.ScheduleDAO
import com.android.calendarapp.feature.schedule.data.entity.ScheduleEntity
import com.android.calendarapp.feature.user.data.entity.UserEntity
import com.android.calendarapp.feature.user.data.dao.UserDAO

@Database(
    entities = [UserEntity::class, CategoryEntity::class, ScheduleEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDAO() : UserDAO

    abstract fun categoryDAO() : CategoryDAO

    abstract fun scheduleDAO() : ScheduleDAO
}