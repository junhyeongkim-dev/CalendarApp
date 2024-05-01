package com.android.calendarapp.library.security.preperence

interface PrefStorageProvider {
    fun setString(key: String, value: String)

    fun getString(key: String) : String

    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String) : Boolean
}