package com.android.calendarapp.library.database.di

import android.content.Context
import androidx.room.Room
import com.android.calendarapp.feature.category.data.dao.CategoryDAO
import com.android.calendarapp.feature.user.data.dao.UserDAO
import com.android.calendarapp.library.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "calendar-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) : UserDAO {
        return database.userInfoDAO()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase) : CategoryDAO {
        return database.categoryDAO()
    }
}