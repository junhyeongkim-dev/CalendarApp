package com.android.calendarapp.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.login.data.LoginFailResponseData
import com.android.calendarapp.feature.login.naver.manager.NaverLoginManager
import com.android.calendarapp.feature.login.naver.response.NaverLoginResponse
import com.android.calendarapp.feature.login.type.LoginType
import com.android.calendarapp.library.database.AppDatabase
import com.android.calendarapp.library.database.user.UserInfo
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.library.security.tink.helper.ITinkHelper
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.output.DialogState
import com.android.calendarapp.ui.common.viewmodel.BaseViewModel
import com.android.calendarapp.ui.login.input.ILoginViewModelInput
import com.android.calendarapp.ui.login.output.ILoginViewModelOutput
import com.android.calendarapp.ui.login.output.LoginNavigateEffect
import com.android.calendarapp.util.ResourceUtil
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val naverLoginManager: NaverLoginManager,
    private val appDatabase: AppDatabase,
    private val preferencesHelper: ISharedPreferencesHelper,
    private val tinkHelper: ITinkHelper,
    private val applicationContext: Context,
) : BaseViewModel(preferencesHelper), ILoginViewModelInput, ILoginViewModelOutput {

    private val _loginNavigateEffect = MutableSharedFlow<LoginNavigateEffect>(replay = 0)
    override val loginNavigateEffect: SharedFlow<LoginNavigateEffect> = _loginNavigateEffect

    override fun login(loginType: LoginType, context: Context) {
        when(loginType) {
            LoginType.GUEST -> TODO()
            LoginType.NAVER -> {
                viewModelScope.launch {
                    naverLoginManager.setLoginResponse(
                        object : NaverLoginResponse<Map<String, Any>>{
                            override fun onSuccess(data: Map<String, Any>) {

                                val userId = data["id"].toString()
                                preferencesHelper.setUserId(userId)

                                val userInfo = UserInfo(
                                    userId = userId,
                                    userName = tinkHelper.stringEncrypt(data["name"].toString(), userId),
                                    if(!data["birthday"]?.toString().isNullOrBlank()) {
                                        tinkHelper.stringEncrypt(data["birthday"].toString(), userId)
                                    } else {
                                        ""
                                    },
                                    userType = LoginType.NAVER
                                )

                                insertUserInfo(userInfo)
                            }

                            override fun onFail(data: LoginFailResponseData) {

                                val content =
                                    ResourceUtil.getString(
                                        applicationContext,
                                        R.string.dialog_login_error_content,
                                        LoginType.NAVER.name,
                                        "${data.code} : (${data.description})"
                                    )

                                val dialogType = AppDialog.DefaultOneButtonDialog(
                                    title = ResourceUtil.getString(
                                        applicationContext,
                                        R.string.dialog_login_error_title
                                    ),
                                    content = content,
                                    confirmOnClick = {
                                        changeDialogUiState(DialogState.Close)
                                    },
                                    onDismiss = {
                                        changeDialogUiState(DialogState.Close)
                                    }
                                )
                                changeDialogUiState(dialogState = DialogState.Open(dialogType))
                            }
                        }
                    )

                    naverLoginManager.login(context)
                }
            }
        }
    }

    private fun insertUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appDatabase.userInfoDAO().insert(userInfo)
            }

            _loginNavigateEffect.emit(LoginNavigateEffect.GoMain)
        }
    }
}