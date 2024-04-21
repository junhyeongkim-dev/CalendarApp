package com.android.calendarapp.library.login.naver.response

import com.android.calendarapp.library.login.model.LoginFailResponseModel

interface NaverLoginResponse<T> {
    fun onSuccess(data: T)
    fun onFail(data: LoginFailResponseModel)
}