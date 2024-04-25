package com.android.calendarapp.feature.category.domain.convert

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.data.entity.CategoryEntity

fun CategoryModel.toEntity(): CategoryEntity = CategoryEntity (
    seqNo = this.seqNo,
    categoryName = this.categoryName
)

fun CategoryEntity.toModel(): CategoryModel = CategoryModel(
    seqNo = this.seqNo,
    categoryName = this.categoryName
)