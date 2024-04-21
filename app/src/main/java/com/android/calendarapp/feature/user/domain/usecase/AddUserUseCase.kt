package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.domain.model.UserModel

interface AddUserUseCase {
    suspend operator fun invoke(userModel: UserModel)
}