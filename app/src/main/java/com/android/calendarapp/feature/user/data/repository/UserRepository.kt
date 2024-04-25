package com.android.calendarapp.feature.user.data.repository

import com.android.calendarapp.feature.user.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun insertUser(userEntity: UserEntity)

    fun selectUser(userId: String) : Flow<UserEntity>

    fun deleteUser(userId: String)
}