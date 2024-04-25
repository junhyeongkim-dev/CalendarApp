package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.data.repository.UserRepository
import com.android.calendarapp.feature.user.domain.convert.toEntity
import com.android.calendarapp.feature.user.domain.model.UserModel
import javax.inject.Inject

class AddUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : AddUserUseCase {
    override suspend fun invoke(userModel: UserModel) =
        userRepository.insertUser(userModel.toEntity())
}