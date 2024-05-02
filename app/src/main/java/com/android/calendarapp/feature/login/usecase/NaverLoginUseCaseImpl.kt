package com.android.calendarapp.feature.login.usecase

import android.content.Context
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.naver.manager.NaverLoginManagerImpl
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NaverLoginUseCaseImpl @Inject constructor(
    private val naverLoginManager: NaverLoginManagerImpl,
    private val prefStorageProvider: PrefStorageProvider,
    private val addUserUseCase: AddUserUseCase
) : NaverLoginUseCase {
    override suspend fun invoke(context: Context, result: Channel<LoginResponseModel>) {
        val scope = CoroutineScope(Dispatchers.Main)

        naverLoginManager.login(
            context,
            object : NaverLoginResponse<Map<String, Any>> {
                override fun onSuccess(data: Map<String, Any>) {

                    val userId = data["id"].toString()
                    prefStorageProvider.setString(PrefStorageConstance.USER_ID_PREF, userId)

                    val userModel = UserModel(
                        userId = userId,
                        userName = data["name"].toString(),
                        userBirth =
                        if(!data["birthday"]?.toString().isNullOrBlank()) {
                            data["birthday"].toString()
                        } else {
                            ""
                        },
                        userType = LoginType.NAVER
                    )

                    scope.launch {
                        addUserUseCase(userModel).also {
                            result.send(
                                LoginResponseModel(
                                    code = LoginConstant.SUCCESS,
                                    description = LoginConstant.SUCCESS_DESC
                                )
                            )
                        }
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