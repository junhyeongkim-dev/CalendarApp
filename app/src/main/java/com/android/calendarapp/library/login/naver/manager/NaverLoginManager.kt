package com.android.calendarapp.library.login.naver.manager

import android.content.Context
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse

interface NaverLoginManager {
    suspend fun login(
        context: Context,
        naverLoginResponse: NaverLoginResponse<Map<String, Any>>
    )
    suspend fun logout(naverLoginResponse: NaverLoginResponse<Boolean>)
    suspend fun refreshToken(naverLoginResponse: NaverLoginResponse<Map<String, Any>>)
}