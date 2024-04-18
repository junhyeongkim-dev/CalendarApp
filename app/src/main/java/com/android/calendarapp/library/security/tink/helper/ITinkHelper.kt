package com.android.calendarapp.library.security.tink.helper

interface ITinkHelper {

    fun stringEncrypt(value: String, associatedData: String) : String
    fun stringDecrypt(value: String, associatedData: String) : String
}