package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.domain.model.UserModel

interface GetUserUseCase {
    suspend operator fun invoke(userId: String) : UserModel
}