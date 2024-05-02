package com.android.calendarapp.ui.splash.output

import kotlinx.coroutines.flow.SharedFlow

interface ISplashOutput {

    // 화면 이동을 위한 로그인 상태값
    val loginState: SharedFlow<SplashNavigateEffect>
}

sealed class SplashNavigateEffect {
    data object Wait : SplashNavigateEffect()
    data object Login : SplashNavigateEffect()
    data class NotLogin(
        val isFail: Boolean
    ) : SplashNavigateEffect()
}