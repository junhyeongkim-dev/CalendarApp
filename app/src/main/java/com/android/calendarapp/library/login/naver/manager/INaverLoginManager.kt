package com.android.calendarapp.library.login.naver.manager

import android.content.Context
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse

interface INaverLoginManager {
    fun setLoginResponse(naverLoginResponse: NaverLoginResponse<Map<String, Any>>)
    suspend fun login(context: Context)
    suspend fun logout()
    suspend fun refreshToken()
}