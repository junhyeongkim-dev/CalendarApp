package com.android.calendarapp.feature.login.naver.manager

import android.content.Context
import com.android.calendarapp.feature.login.naver.response.NaverLoginResponse
import kotlinx.coroutines.flow.SharedFlow

interface INaverLoginManager {
    fun setLoginResponse(naverLoginResponse: NaverLoginResponse<Map<String, Any>>)
    suspend fun login(context: Context)
    suspend fun logout()
    suspend fun refreshToken()
}