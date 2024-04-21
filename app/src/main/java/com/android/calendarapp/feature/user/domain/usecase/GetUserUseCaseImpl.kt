package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.data.repository.UserRepository
import com.android.calendarapp.feature.user.domain.convert.toModel
import com.android.calendarapp.feature.user.domain.model.UserModel
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserUseCase {
    override suspend fun invoke(userId: String) : UserModel {
        return userRepository.selectUser(userId).toModel()
    }
}