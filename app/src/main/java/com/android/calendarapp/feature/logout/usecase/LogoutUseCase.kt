package com.android.calendarapp.feature.logout.usecase

import com.android.calendarapp.library.login.model.LoginResponseModel
import kotlinx.coroutines.channels.Channel

interface LogoutUseCase {
    suspend operator fun invoke(result: Channel<LoginResponseModel>)
}