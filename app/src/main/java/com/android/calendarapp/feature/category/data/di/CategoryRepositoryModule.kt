package com.android.calendarapp.feature.category.data.di

import com.android.calendarapp.feature.category.data.repository.CategoryRepositoryImpl
import com.android.calendarapp.feature.category.data.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCategoryRepo(categoryRepositoryImpl: CategoryRepositoryImpl) : CategoryRepository
}