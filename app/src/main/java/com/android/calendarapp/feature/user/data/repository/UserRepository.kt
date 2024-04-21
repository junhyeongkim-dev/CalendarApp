package com.android.calendarapp.feature.user.data.repository

import com.android.calendarapp.feature.user.data.entity.UserEntity

interface UserRepository {
    fun insertUser(userEntity: UserEntity)

    fun selectUser(userId: String) : UserEntity

    fun deleteUser(userId: String)
}