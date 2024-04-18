package com.android.calendarapp.feature.login.naver.manager

import android.content.Context
import kotlinx.coroutines.flow.SharedFlow

interface INaverLoginManager {
    suspend fun login(context: Context)
    suspend fun logout()
    fun getUserProfile()
}