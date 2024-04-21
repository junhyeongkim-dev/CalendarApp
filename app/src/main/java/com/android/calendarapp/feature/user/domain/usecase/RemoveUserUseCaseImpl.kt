package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.data.repository.UserRepository
import javax.inject.Inject

class RemoveUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : RemoveUserUseCase {
    override suspend fun invoke(userId: String) {
        userRepository.deleteUser(userId)
    }
}