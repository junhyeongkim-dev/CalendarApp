package com.android.calendarapp.ui.login.input

import android.content.Context
import com.android.calendarapp.library.login.type.LoginType

interface ILoginViewModelInput {
    fun login(loginType: LoginType, context: Context)
}