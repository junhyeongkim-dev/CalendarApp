package com.android.calendarapp.library.security.preperence.helper

import com.android.calendarapp.library.security.preperence.AppEncryptedSharedPreferences
import com.android.calendarapp.library.security.preperence.constants.SharedPreferenceKeys
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(
    private val preferences: AppEncryptedSharedPreferences
) : ISharedPreferencesHelper {

    override fun setUserId(value: String) {
        preferences.setString(SharedPreferenceKeys.USER_ID_PREF, value)
    }

    override fun getUserId(): String {
        return preferences.getString(SharedPreferenceKeys.USER_ID_PREF)
    }
}