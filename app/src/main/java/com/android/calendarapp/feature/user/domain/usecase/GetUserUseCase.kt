package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface GetUserUseCase {
    suspend operator fun invoke() : Flow<UserModel>
}