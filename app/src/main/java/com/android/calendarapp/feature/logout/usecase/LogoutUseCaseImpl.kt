package com.android.calendarapp.feature.logout.usecase

import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val naverLoginManager: NaverLoginManager,
    private val prefStorageProvider: PrefStorageProvider
) : LogoutUseCase {
    override suspend fun invoke(result: Channel<LoginResponseModel>) {
        val scope = CoroutineScope(Dispatchers.IO)

        getUserUseCase().also { userModelFlow ->
            userModelFlow.collect { userModel ->
                if(userModel.userType == LoginType.NAVER) {
                    // 로그인 타입이 네이버

                    naverLoginManager.logout(
                        object : NaverLoginResponse<Boolean> {
                            override fun onSuccess(data: Boolean) {

                                // 저장된 로그인 아이디 삭제
                                prefStorageProvider.setString(PrefStorageConstance.USER_ID_PREF, "")

                                scope.launch {
                                    result.send(
                                        LoginResponseModel(
                                            code = LoginConstant.SUCCESS,
                                            description = LoginConstant.SUCCESS_DESC
                                        )
                                    )
                                }
                            }

                            override fun onFail(data: LoginResponseModel) {
                                scope.launch {
                                    result.send(data)
                                }
                            }
                        }
                    )
                }else {
                    prefStorageProvider.setString(PrefStorageConstance.USER_ID_PREF, "")
                    scope.launch {
                        result.send(
                            LoginResponseModel(
                                code = LoginConstant.SUCCESS,
                                description = LoginConstant.SUCCESS_DESC
                            )
                        )
                    }
                }
            }
        }
    }
}