package com.android.calendarapp.feature.login.usecase

import com.android.calendarapp.library.login.model.LoginResponseModel
import kotlinx.coroutines.channels.Channel

interface NaverRefreshUseCase {
    suspend operator fun invoke(result: Channel<LoginResponseModel>)
}