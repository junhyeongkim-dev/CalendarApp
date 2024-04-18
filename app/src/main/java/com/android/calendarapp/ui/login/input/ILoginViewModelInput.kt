package com.android.calendarapp.ui.login.input

import android.content.Context
import com.android.calendarapp.feature.login.type.LoginType

interface ILoginViewModelInput {
    fun login(loginType: LoginType, context: Context)
}