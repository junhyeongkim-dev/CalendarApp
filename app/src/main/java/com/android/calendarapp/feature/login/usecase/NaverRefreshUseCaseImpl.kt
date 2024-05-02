package com.android.calendarapp.feature.login.usecase

import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NaverRefreshUseCaseImpl @Inject constructor(
    private val naverLoginManager: NaverLoginManager
) : NaverRefreshUseCase {
    override suspend fun invoke(result: Channel<LoginResponseModel>) {
        val scope = CoroutineScope(Dispatchers.Main)

        naverLoginManager.refreshToken(
            object : NaverLoginResponse<Map<String, Any>> {
                override fun onSuccess(data: Map<String, Any>) {
                    scope.launch {
                        result.send(
                            LoginResponseModel(
                                code = LoginConstant.SUCCESS,
                                description = LoginConstant.SUCCESS_DESC
                            )
                        )
                    }
                }

                override fun onFail(errorData: LoginResponseModel) {
                    scope.launch {
                        result.send(errorData)
                    }
                }
            }
        )
    }
}