package com.android.calendarapp.feature.login.naver.response

import com.android.calendarapp.feature.login.data.LoginFailResponseData

interface NaverLoginResponse<T> {
    fun onSuccess(data: T)
    fun onFail(data: LoginFailResponseData)
}