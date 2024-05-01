package com.android.calendarapp.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryListUseCase
import com.android.calendarapp.feature.login.usecase.GuestLoginUseCase
import com.android.calendarapp.feature.login.usecase.NaverLoginUseCase
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.login.input.ILoginInput
import com.android.calendarapp.ui.login.output.ILoginViewModelOutput
import com.android.calendarapp.ui.login.output.LoginNavigateEffect
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addCategoryListUseCase: AddCategoryListUseCase,
    private val naverLoginUseCase: NaverLoginUseCase,
    private val guestLoginUseCase: GuestLoginUseCase
) : BaseViewModel(), ILoginInput, ILoginViewModelOutput {

    private val _loginNavigateEffect = MutableSharedFlow<LoginNavigateEffect>(replay = 0)
    override val loginNavigateEffect: SharedFlow<LoginNavigateEffect> = _loginNavigateEffect

    private val resultChannel: Channel<LoginResponseModel> = Channel()

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            resultChannel.consumeEach { result ->
                if(result.code == LoginConstant.SUCCESS) {
                    // 로그인 성공

                    addCategoryListUseCase()

                    _loginNavigateEffect.emit(LoginNavigateEffect.GoMain)
                }else {
                    // 로그인 실패

                    val dialogType = AppDialog.DefaultOneButtonDialog(
                        title = ResourceUtil.getString(
                            applicationContext,
                            R.string.dialog_login_error_title
                        ),
                        content =
                            ResourceUtil.getString(
                                applicationContext,
                                R.string.dialog_login_error_content,
                                LoginType.NAVER.name,
                                "${result.code} : (${result.description})"
                            ),
                        confirmOnClick = {
                            onDismissDialog()
                        },
                        onDismiss = {
                            onDismissDialog()
                        }
                    )

                    showDialog(
                        dialogUiState = DialogUiState.Show(
                            dialogType = dialogType,
                        )
                    )
                }
            }
        }
    }

    override fun login(loginType: LoginType, context: Context) {
        when(loginType) {
            LoginType.GUEST -> {
                viewModelScope.launch {
                    guestLoginUseCase(
                        result = resultChannel
                    )
                }
            }

            LoginType.NAVER -> {
                viewModelScope.launch {

                    naverLoginUseCase(
                        context = context,
                        result = resultChannel
                    )
                }
            }
        }
    }
}