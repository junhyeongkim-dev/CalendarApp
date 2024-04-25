package com.android.calendarapp.ui.splash.output

import kotlinx.coroutines.flow.SharedFlow

interface ISplashOutput {
    val loginState: SharedFlow<LoginStateEffect>
}

sealed class LoginStateEffect {
    data object Wait : LoginStateEffect()
    data object Login : LoginStateEffect()
    data class NotLogin(
        val isFail: Boolean
    ) : LoginStateEffect()
}