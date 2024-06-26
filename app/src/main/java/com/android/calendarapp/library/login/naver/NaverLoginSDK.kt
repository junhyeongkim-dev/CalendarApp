package com.android.calendarapp.library.login.naver

import android.content.Context
import com.android.calendarapp.BuildConfig
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import javax.inject.Inject

class NaverLoginSDK @Inject constructor(
    private val applicationContext: Context
) {
    init {
        NaverIdLoginSDK.initialize(applicationContext, BuildConfig.OAUTH_CLIENT_ID, BuildConfig.OAUTH_CLIENT_SECRET, BuildConfig.OAUTH_CLIENT_NAME)
    }

    fun login(context: Context, oAuthLoginCallback: OAuthLoginCallback) {
        NaverIdLoginSDK.authenticate(context, oAuthLoginCallback)
    }

    fun refreshAccessToken(oAuthLoginCallback: OAuthLoginCallback) {
        NidOAuthLogin().callRefreshAccessTokenApi(oAuthLoginCallback)
    }

    fun reLogin(oAuthLoginCallback: OAuthLoginCallback) {
//        NaverIdLoginSDK.reagreeAuthenticate(context, oAuthLoginCallback)
    }

    fun logout(oAuthLoginCallback: OAuthLoginCallback) {
        NidOAuthLogin().callDeleteTokenApi(oAuthLoginCallback)
//        NaverIdLoginSDK.logout()
    }

    fun getLoginUserProfile(nidProfileCallback: NidProfileCallback<NidProfileMap>) {
        NidOAuthLogin().getProfileMap(nidProfileCallback)
    }
}