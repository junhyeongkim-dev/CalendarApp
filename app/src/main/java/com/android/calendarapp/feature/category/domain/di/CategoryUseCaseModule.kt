package com.android.calendarapp.feature.category.domain.di

import com.android.calendarapp.feature.category.domain.usecase.AddCategoryListUseCaseImpl
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCaseImpl
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCaseImpl
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryListUseCase
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryGroupListUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryGroupListUseCaseImpl
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.feature.category.domain.usecase.RemoveCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.RemoveCategoryUseCaseImpl
import com.android.calendarapp.feature.category.domain.usecase.UpdateCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.UpdateCategoryUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAddCategoryListUseCase(addCategoryListUseCaseImpl: AddCategoryListUseCaseImpl) : AddCategoryListUseCase

    @Binds
    @Singleton
    abstract fun bindAddCategoryUseCase(addCategoryUseCaseImpl: AddCategoryUseCaseImpl) : AddCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryListUseCase(getCategoryListUseCaseImpl: GetCategoryListUseCaseImpl) : GetCategoryListUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryGroupListUseCase(getCategoryGroupListUseCaseImpl: GetCategoryGroupListUseCaseImpl) : GetCategoryGroupListUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateCategoryUseCase(updateCategoryUseCaseImpl: UpdateCategoryUseCaseImpl) : UpdateCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindRemoveCategoryUseCase(removeCategoryUseCaseImpl: RemoveCategoryUseCaseImpl) : RemoveCategoryUseCase
}