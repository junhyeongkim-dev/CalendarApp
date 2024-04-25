package com.android.calendarapp.feature.user.data.repository

import com.android.calendarapp.feature.user.data.dao.UserDAO
import com.android.calendarapp.feature.user.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDAO: UserDAO
) : UserRepository {
    override fun insertUser(userEntity: UserEntity) {
        userDAO.insert(userEntity)
    }

    override fun selectUser(userId: String) : Flow<UserEntity> {
        return userDAO.getUser(userId)
    }

    override fun deleteUser(userId: String) {
        userDAO.delete(userId)
    }
}