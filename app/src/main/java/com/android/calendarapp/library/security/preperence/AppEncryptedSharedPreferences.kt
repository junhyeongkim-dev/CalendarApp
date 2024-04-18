package com.android.calendarapp.library.security.preperence

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

/**
 * 암호화된 SharedPreferences
 */
class AppEncryptedSharedPreferences @Inject constructor(
    context: Context
) {

    private val sharedPreferences: SharedPreferences

    init {
        val alias = "calendar_prefs"
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            alias,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String) : String {
        return sharedPreferences.getString(key, "").toString()
    }

    fun setInt(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun getInt(key: String) : Int {
        return sharedPreferences.getInt(key, 0)
    }
}