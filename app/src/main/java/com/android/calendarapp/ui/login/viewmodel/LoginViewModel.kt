package com.android.calendarapp.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryListUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.library.login.model.LoginFailResponseModel
import com.android.calendarapp.library.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.login.naver.response.NaverLoginResponse
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.login.input.ILoginInput
import com.android.calendarapp.ui.login.output.ILoginViewModelOutput
import com.android.calendarapp.ui.login.output.LoginNavigateEffect
import com.android.calendarapp.util.ResourceUtil
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
    private val preferencesHelper: ISharedPreferencesHelper,
    private val applicationContext: Context,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val addCategoryListUseCase: AddCategoryListUseCase,
    private val addUserUseCase: AddUserUseCase
) : BaseViewModel(), ILoginInput, ILoginViewModelOutput {

    private val _loginNavigateEffect = MutableSharedFlow<LoginNavigateEffect>(replay = 0)
    override val loginNavigateEffect: SharedFlow<LoginNavigateEffect> = _loginNavigateEffect

    override fun login(loginType: LoginType, context: Context, currentRoute: String) {
        when(loginType) {
            LoginType.GUEST -> TODO()
            LoginType.NAVER -> {
                viewModelScope.launch {
                    naverLoginManager.setLoginResponse(
                        object : NaverLoginResponse<Map<String, Any>> {
                            override fun onSuccess(data: Map<String, Any>) {

                                val userId = data["id"].toString()
                                preferencesHelper.setUserId(userId)

                                val userModel = UserModel(
                                    userId = userId,
                                    userName = data["name"].toString(),
                                    if(!data["birthday"]?.toString().isNullOrBlank()) {
                                        data["birthday"].toString()
                                    } else {
                                        ""
                                    },
                                    userType = LoginType.NAVER
                                )

                                viewModelScope.launch {
                                    withContext(Dispatchers.IO) {

                                        addUserUseCase(userModel)

                                        checkDefaultCategoryDataAndInsert()
                                    }

                                    _loginNavigateEffect.emit(LoginNavigateEffect.GoMain)
                                }
                            }

                            override fun onFail(data: LoginFailResponseModel) {

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
                                        onDismissDialog()
                                    },
                                    onDismiss = {
                                        onDismissDialog()
                                    }
                                )
                                showDialog(
                                    dialogUiState = DialogUiState.Show(
                                        route = currentRoute,
                                        dialogType = dialogType,
                                    )
                                )
                            }
                        }
                    )

                    naverLoginManager.login(context)
                }
            }
        }
    }

    // 기본 카테고리 세팅
    private fun checkDefaultCategoryDataAndInsert() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryListUseCase().collect { categoryList ->
                if(categoryList.isEmpty()) {
                    addCategoryListUseCase()
                }
            }
        }
    }
}