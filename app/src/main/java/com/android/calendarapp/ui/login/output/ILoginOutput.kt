package com.android.calendarapp.ui.login.output

import kotlinx.coroutines.flow.SharedFlow

interface ILoginViewModelOutput {
    val loginNavigateEffect: SharedFlow<LoginNavigateEffect>
}

sealed class LoginNavigateEffect {
    data object GoMain : LoginNavigateEffect()
}