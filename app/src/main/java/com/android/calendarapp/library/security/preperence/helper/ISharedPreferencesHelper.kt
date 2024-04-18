package com.android.calendarapp.library.security.preperence.helper

interface ISharedPreferencesHelper {
    fun setUserId(value: String)
    fun getUserId() : String
}