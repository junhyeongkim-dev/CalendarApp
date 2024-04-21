package com.android.calendarapp.feature.user.domain.usecase

interface RemoveUserUseCase {
    suspend operator fun invoke(userId: String)
}