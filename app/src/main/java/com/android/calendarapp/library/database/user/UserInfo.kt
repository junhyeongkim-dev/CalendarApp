package com.android.calendarapp.library.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.calendarapp.feature.login.type.LoginType

@Entity
class UserInfo(
    @PrimaryKey
    @ColumnInfo(name="user_id")
    var userId: String,

    @ColumnInfo(name="user_name")
    var userName: String,

    @ColumnInfo(name="user_birth")
    var userBirth: String,

    @ColumnInfo(name="user_type")
    var userType: LoginType
)