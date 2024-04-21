package com.android.calendarapp.feature.user.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.calendarapp.library.login.type.LoginType

@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey
    @ColumnInfo(name="user_id")
    val userId: String,

    @ColumnInfo(name="user_name")
    val userName: String,

    @ColumnInfo(name="user_birth")
    val userBirth: String,

    @ColumnInfo(name="user_type")
    val userType: LoginType
)