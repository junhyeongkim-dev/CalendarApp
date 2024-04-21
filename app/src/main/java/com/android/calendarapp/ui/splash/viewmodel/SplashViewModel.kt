package com.android.calendarapp.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.android.calendarapp.library.login.model.LoginFailResponseModel
import com.android.calendarapp.library.login.naver.manager.INaverLoginManager
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.ui.common.viewmodel.BaseViewModel
import com.android.calendarapp.ui.splash.input.ISplashViewModelInput
import com.android.calendarapp.ui.splash.output.ISplashViewModelOutput
import com.android.calendarapp.ui.splash.output.LoginStateEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesHelper: ISharedPreferencesHelper,
    private val naverLoginManager: INaverLoginManager
) : BaseViewModel(preferencesHelper), ISplashViewModelInput, ISplashViewModelOutput {

    private val _loginState: MutableSharedFlow<LoginStateEffect> = MutableSharedFlow(replay = 0)
    override val loginState: SharedFlow<LoginStateEffect> = _loginState

    override fun checkValidLogin() {
        viewModelScope.launch {
            if(preferencesHelper.getUserId().isNotEmpty()) {
                // 로그인 되어있는 상태일 때 유저 프로필을 조회하여 네이버 로그인 여부 확인

                naverLoginManager.setLoginResponse(
                    object : NaverLoginResponse<Map<String, Any>> {
                        override fun onSuccess(data: Map<String, Any>) {
                            viewModelScope.launch {
                                _loginState.emit(LoginStateEffect.Login)
                            }
                        }

                        override fun onFail(data: LoginFailResponseModel) {
                            viewModelScope.launch {
                                _loginState.emit(
                                    LoginStateEffect.NotLogin(true))
                            }
                        }
                    }
                )

                naverLoginManager.refreshToken()
            }else {
                viewModelScope.launch {
                    _loginState.emit(
                        LoginStateEffect.NotLogin(false))
                }
            }
        }
    }
}