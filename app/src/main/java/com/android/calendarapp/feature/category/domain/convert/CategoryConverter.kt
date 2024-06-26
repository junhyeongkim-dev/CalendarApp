package com.android.calendarapp.feature.category.domain.convert

import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.data.entity.CategoryEntity
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel

fun CategoryModel.toEntity(userId: String): CategoryEntity = CategoryEntity (
    seqNo = this.seqNo,
    categoryName = this.categoryName,
    userId = userId
)

fun CategoryEntity.toModel(): CategoryModel = CategoryModel(
    seqNo = this.seqNo,
    categoryName = this.categoryName
)

fun CategoryGroupModel.toEntity(): CategoryEntity = CategoryEntity(
    seqNo = this.seqNo,
    categoryName = "",
    userId = ""
)