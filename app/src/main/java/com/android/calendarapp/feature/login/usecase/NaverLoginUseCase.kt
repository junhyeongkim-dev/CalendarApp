package com.android.calendarapp.feature.login.usecase

import android.content.Context
import com.android.calendarapp.library.login.model.LoginResponseModel
import kotlinx.coroutines.channels.Channel

interface NaverLoginUseCase {
    suspend operator fun invoke(context: Context, result: Channel<LoginResponseModel>)
}