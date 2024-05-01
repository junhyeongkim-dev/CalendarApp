package com.android.calendarapp.library.security.preperence

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

/**
 * 암호화된 SharedPreferences
 */
class PrefStorageProviderImpl @Inject constructor(
    context: Context
) : PrefStorageProvider {
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

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    override fun getString(key: String) : String {
        return sharedPreferences.getString(key, "").toString()
    }

    override fun setBoolean(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    override fun getBoolean(key: String) : Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}