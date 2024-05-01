package com.android.calendarapp.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.splash.input.ISplashInput
import com.android.calendarapp.ui.splash.output.ISplashOutput
import com.android.calendarapp.ui.splash.output.LoginStateEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefStorageProvider: PrefStorageProvider,
    private val getUserUseCase: GetUserUseCase,
    private val naverLoginManager: NaverLoginManager
) : BaseViewModel(), ISplashInput, ISplashOutput {

    // 화면 이동을 위한 로그인 상태값
    private val _loginState: MutableSharedFlow<LoginStateEffect> = MutableSharedFlow(replay = 0)
    override val loginState: SharedFlow<LoginStateEffect> = _loginState

    // 로그인 여부 확인
    override fun checkIsLogin() {
        viewModelScope.launch {
            if(prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF).isNotEmpty()) {

                getUserUseCase().collect { userModel ->
                    if(userModel.userType == LoginType.NAVER) {
                        // 로그인 되어있는 상태일 때 유저 프로필을 조회하여 네이버 로그인 여부 확인

                        naverLoginManager.refreshToken(
                            object : NaverLoginResponse<Map<String, Any>> {
                                override fun onSuccess(data: Map<String, Any>) {
                                    viewModelScope.launch {
                                        _loginState.emit(LoginStateEffect.Login)
                                    }
                                }

                                override fun onFail(data: LoginResponseModel) {
                                    viewModelScope.launch {
                                        _loginState.emit(
                                            LoginStateEffect.NotLogin(true))
                                    }
                                }
                            }
                        )
                    }else {
                        viewModelScope.launch {
                            _loginState.emit(LoginStateEffect.Login)
                        }
                    }
                }
            }else {
                viewModelScope.launch {
                    _loginState.emit(
                        LoginStateEffect.NotLogin(false))
                }
            }
        }
    }
}