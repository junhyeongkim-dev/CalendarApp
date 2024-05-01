package com.android.calendarapp.library.login.naver.manager

import android.content.Context
import com.android.calendarapp.BuildConfig
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.naver.NaverLoginSDK
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthErrorCode
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileMap
import javax.inject.Inject

class NaverLoginManagerImpl @Inject constructor(
    private val naverLoginSDK: NaverLoginSDK
) : NaverLoginManager {

    init {
        if (BuildConfig.DEBUG) {
            NaverIdLoginSDK.showDevelopersLog(true)
        }else {
            NaverIdLoginSDK.showDevelopersLog(false)
        }
    }

    // 네이버 로그인
    override suspend fun login(
        context: Context,
        naverLoginResponse: NaverLoginResponse<Map<String, Any>>
    ) {
        naverLoginSDK.login(
            context,
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    getUserProfile(naverLoginResponse)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onLoginFail(naverLoginResponse)
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    override suspend fun refreshToken(naverLoginResponse: NaverLoginResponse<Map<String, Any>>) {
        naverLoginSDK.refreshAccessToken(
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    getUserProfile(naverLoginResponse)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onLoginFail(naverLoginResponse)
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    // 사용자 프로필 정보 호출
    private fun getUserProfile(naverLoginResponse: NaverLoginResponse<Map<String, Any>>) {
        naverLoginSDK.getLoginUserProfile(
            object : NidProfileCallback<NidProfileMap> {
                override fun onSuccess(result: NidProfileMap) {
                    naverLoginResponse.onSuccess(result.profile!!)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    onLoginFail(naverLoginResponse)
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }
        )
    }

    // 네이버 로그아웃
    override suspend fun logout(naverLoginResponse: NaverLoginResponse<Boolean>) {
        naverLoginSDK.logout(object : OAuthLoginCallback {
            override fun onSuccess() {
                naverLoginResponse.onSuccess(true)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                if(NaverIdLoginSDK.getLastErrorCode() != NidOAuthErrorCode.CLIENT_USER_CANCEL) {
                    // 사용자 취소가 아닌 에러일 때

                    val responseData = LoginResponseModel(
                        NaverIdLoginSDK.getLastErrorCode().code,
                        NaverIdLoginSDK.getLastErrorDescription() ?: ""
                    )

                    naverLoginResponse.onFail(responseData)
                }
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }

    // 네이버 로그인 실패 처리
    private fun onLoginFail(naverLoginResponse: NaverLoginResponse<Map<String, Any>>) {
        if(NaverIdLoginSDK.getLastErrorCode() != NidOAuthErrorCode.CLIENT_USER_CANCEL) {
            // 사용자 취소가 아닌 에러일 때

            val responseData = LoginResponseModel(
                NaverIdLoginSDK.getLastErrorCode().code,
                NaverIdLoginSDK.getLastErrorDescription() ?: ""
            )

            naverLoginResponse.onFail(responseData)
        }
    }
}