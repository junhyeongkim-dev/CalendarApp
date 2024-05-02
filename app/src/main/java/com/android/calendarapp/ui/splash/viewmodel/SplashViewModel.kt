package com.android.calendarapp.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.android.calendarapp.feature.login.usecase.NaverRefreshUseCase
import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.splash.input.ISplashInput
import com.android.calendarapp.ui.splash.output.ISplashOutput
import com.android.calendarapp.ui.splash.output.SplashNavigateEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefStorageProvider: PrefStorageProvider,
    private val getUserUseCase: GetUserUseCase,
    private val naverRefreshUseCase: NaverRefreshUseCase
) : BaseViewModel(), ISplashInput, ISplashOutput {

    // 화면 이동을 위한 로그인 상태값
    private val _loginState: MutableSharedFlow<SplashNavigateEffect> = MutableSharedFlow(replay = 0)
    override val loginState: SharedFlow<SplashNavigateEffect> = _loginState

    private val resultChannel: Channel<LoginResponseModel> = Channel()

    init {
        viewModelScope.launch {
            resultChannel.consumeEach { loginResponseModel ->
                if(loginResponseModel.code == LoginConstant.SUCCESS) {
                    // 네이버 로그인 성공

                    login()
                }else{
                    notLogin(true)
                }
            }
        }
    }

    // 로그인 여부 확인
    override fun checkIsLogin() {
        viewModelScope.launch {
            if(prefStorageProvider.getString(PrefStorageConstance.USER_ID_PREF).isNotEmpty()) {
                // 로컬에 저장된 유저 id가 있을 때

                getUserUseCase().collect { userModel ->
                    if(userModel.userType == LoginType.NAVER) {
                        // 로그인 되어있는 상태일 때 유저 프로필을 조회하여 네이버 로그인 여부 확인

                        naverRefreshUseCase(resultChannel)
                    }else {
                        // 게스트는 바로 로그인 처리

                        login()
                    }
                }
            }else {
                notLogin(false)
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _loginState.emit(SplashNavigateEffect.Login)
        }
    }

    private fun notLogin(isFail: Boolean) {
        viewModelScope.launch {
            _loginState.emit(SplashNavigateEffect.NotLogin(isFail))
        }
    }
}