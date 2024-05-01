package com.android.calendarapp.feature.category.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
class CategoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("seq_no")
    val seqNo: Int = 0,

    @ColumnInfo("category_name")
    val categoryName: String,

    @ColumnInfo("user_id")
    val userId: String
)