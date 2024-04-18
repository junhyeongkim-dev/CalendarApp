package com.android.calendarapp.library.security.tink.helper


import android.util.Base64
import com.android.calendarapp.library.security.tink.AppTink
import javax.inject.Inject

class TinkHelper @Inject constructor(
    private val appTink: AppTink
) : ITinkHelper {

    // String 값 tink 암호화
    override fun stringEncrypt(value: String, associatedData: String) : String {
        val encValue = appTink.encrypt(value.toByteArray(), associatedData.toByteArray())
        return Base64.encodeToString(encValue, Base64.DEFAULT)
    }

    // String 값 tink 암호화
    override fun stringDecrypt(value: String, associatedData: String) : String {
        val decValue = appTink.decrypt(Base64.decode(value, Base64.DEFAULT), associatedData.toByteArray())

        return String(decValue)
    }
}