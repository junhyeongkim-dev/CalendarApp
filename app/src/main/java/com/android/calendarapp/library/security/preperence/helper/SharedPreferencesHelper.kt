package com.android.calendarapp.library.security.preperence.helper

import com.android.calendarapp.library.security.preperence.AppEncryptedSharedPreferences
import com.android.calendarapp.library.security.preperence.constants.SharedPreferenceConstance
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(
    private val preferences: AppEncryptedSharedPreferences
) : ISharedPreferencesHelper {

    override fun setUserId(value: String) {
        preferences.setString(SharedPreferenceConstance.USER_ID_PREF, value)
    }

    override fun getUserId(): String {
        return preferences.getString(SharedPreferenceConstance.USER_ID_PREF)
    }
}