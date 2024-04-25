package com.android.calendarapp.feature.user.domain.model

import com.android.calendarapp.library.login.type.LoginType

data class UserModel(
    val userId: String = "",

    val userName: String = "",

    val userBirth: String = "",

    val userType: LoginType = LoginType.GUEST
)
