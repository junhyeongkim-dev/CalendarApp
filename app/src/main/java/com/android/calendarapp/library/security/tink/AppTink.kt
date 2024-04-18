package com.android.calendarapp.library.security.tink

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import javax.inject.Inject


class AppTink @Inject constructor(
    private val context: Context
) {

    private val aead: Aead

    private val KEY_SET_NAME = "key_set_calendar"
    private val PREF_FILE_NAME = "pref_calendar"
    private val MASTER_KEY = "master_Key_calendar"

    init {
        // AEAD(Authenticated Encryption with Associated Data) 설정을 등록합니다.
        AeadConfig.register();

        // 아래 처럼 키셋을 설정해야 앱 업데이트 시 키가 유지가 됨
        // AES256_GCM 템플릿을 사용하여 새 키셋 핸들을 생성합니다.
        // AndroidKeysetManager를 사용하여 키셋을 생성하거나 기존 키셋을 가져옴
        val keySetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEY_SET_NAME, PREF_FILE_NAME)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(AndroidKeystoreKmsClient.PREFIX + MASTER_KEY)
            .build()
            .keysetHandle


            /*// KeysetManager를 사용하여 키셋을 관리합니다.
            val keysetManager = KeysetManager.withKeysetHandle(keysetHandle)
            // 새로운 키를 키셋에 추가합니다. 여기서는 AES256_GCM 템플릿을 사용합니다.
            .add(AeadKeyTemplates.AES256_GCM)
            // 변경 사항을 적용합니다.
            .getKeysetHandle()*/

        // 아래만 세팅하면 앱이 업데이트 시 키가 바뀌어 복호화가 제대로 안됨
//        val keySetHandle: KeysetHandle = KeysetHandle.generateNew(PredefinedAeadParameters.AES256_GCM)

        aead = keySetHandle.getPrimitive(Aead::class.java)
    }

    // 암호화할 때 사용한 associatedData의 값과 복호화할 때 사용한 associatedData의 값이 같아야 복호화가 정상적으로 수행됨
    fun encrypt(value: ByteArray, associatedData: ByteArray) : ByteArray {
        return aead.encrypt(value, associatedData)
    }

    fun decrypt(value: ByteArray, associatedData: ByteArray) : ByteArray {
        return aead.decrypt(value, associatedData)
    }
}