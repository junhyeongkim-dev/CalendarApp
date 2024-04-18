package com.android.calendarapp.feature.login.naver.manager

import android.content.Context
import com.android.calendarapp.BuildConfig
import com.android.calendarapp.feature.login.data.LoginFailResponseData
import com.android.calendarapp.feature.login.naver.NaverLoginSDK
import com.android.calendarapp.feature.login.naver.response.NaverLoginResponse
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthErrorCode
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import javax.inject.Inject

class NaverLoginManager @Inject constructor(
    private val naverLoginSDK: NaverLoginSDK
) : INaverLoginManager {

    lateinit var naverLoginResponse: NaverLoginResponse<Map<String, Any>>

    init {
        if (BuildConfig.DEBUG) {
            NaverIdLoginSDK.showDevelopersLog(true)
        }else {
            NaverIdLoginSDK.showDevelopersLog(false)
        }
    }

    override fun setLoginResponse(naverLoginResponse: NaverLoginResponse<Map<String, Any>>) {
        this.naverLoginResponse = naverLoginResponse
    }

    // 네이버 로그인
    override suspend fun login(context: Context) {
        naverLoginSDK.login(
            context,
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    getUserProfile()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onFail()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    override suspend fun refreshToken() {
        naverLoginSDK.refreshAccessToken(
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    getUserProfile()
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onFail()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    // 사용자 프로필 정보 호출
    private fun getUserProfile() {
        naverLoginSDK.getLoginUserProfile(
            object : NidProfileCallback<NidProfileMap> {
                override fun onSuccess(result: NidProfileMap) {
                    if(::naverLoginResponse.isInitialized) {
                        naverLoginResponse.onSuccess(result.profile!!)
                    }
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onFail()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    // 네이버 로그아웃
    override suspend fun logout() {
        naverLoginSDK.logout(object : OAuthLoginCallback {
            override fun onSuccess() {

            }

            override fun onFailure(httpStatus: Int, message: String) {
                onFail()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    // 네이버 로그인 실패 처리
    private fun onFail() {
        if(NaverIdLoginSDK.getLastErrorCode() != NidOAuthErrorCode.CLIENT_USER_CANCEL) {
            // 사용자 취소가 아닌 에러일 때

            val responseData = LoginFailResponseData(
                NaverIdLoginSDK.getLastErrorCode().code,
                NaverIdLoginSDK.getLastErrorDescription() ?: ""
            )

            if(::naverLoginResponse.isInitialized) {
                naverLoginResponse.onFail(responseData)
            }
        }
    }
}