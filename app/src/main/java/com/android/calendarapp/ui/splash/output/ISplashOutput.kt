package com.android.calendarapp.ui.splash.output

import kotlinx.coroutines.flow.SharedFlow

interface ISplashOutput {

    // 화면 이동을 위한 로그인 상태값
    val loginState: SharedFlow<LoginStateEffect>
}

sealed class LoginStateEffect {
    data object Wait : LoginStateEffect()
    data object Login : LoginStateEffect()
    data class NotLogin(
        val isFail: Boolean
    ) : LoginStateEffect()
}