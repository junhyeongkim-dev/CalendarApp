package com.android.calendarapp.ui.common.popup.config.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.logout.usecase.LogoutUseCase
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.config.input.IConfigPopupInput
import com.android.calendarapp.ui.common.popup.config.output.ConfigDialog
import com.android.calendarapp.ui.common.popup.config.output.IConfigPopupOutput
import com.android.calendarapp.ui.common.popup.config.output.NavigateEffect
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addUserUseCase: AddUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel(), IConfigPopupInput, IConfigPopupOutput {

    val input: IConfigPopupInput = this

    private val _configPopupUiState: MutableState<Boolean> = mutableStateOf(false)
    override val configPopupUiState: State<Boolean> = _configPopupUiState

    private val _configDialogUiState: MutableStateFlow<ConfigDialog> = MutableStateFlow(ConfigDialog.Dismiss)
    override val configDialogUiState: StateFlow<ConfigDialog> = _configDialogUiState

    private val _userNameEditText: MutableState<String> = mutableStateOf("")
    override val userNameEditText: State<String> = _userNameEditText

    private val _navigationState: MutableSharedFlow<NavigateEffect> = MutableSharedFlow(replay = 0)
    override val navigationState: SharedFlow<NavigateEffect> = _navigationState

    // 현재 저장되어 있는 유저 네임
    private lateinit var savedUserModel: UserModel

    private var dialogChannel: Channel<DialogUiState> = Channel()
    private val logoutChannel: Channel<LoginResponseModel> = Channel()

    init {
        viewModelScope.launch {
            logoutChannel.consumeEach { logoutResponseModel ->
                if(logoutResponseModel.code == LoginConstant.SUCCESS) {
                    // 로그아웃 성공

                    navigateUi(NavigateEffect.GoLogin)
                }else {
                    dialogChannel.send(
                        DialogUiState.Show(
                            dialogType = AppDialog.DefaultOneButtonDialog(
                                title = ResourceUtil.getString(applicationContext, R.string.dialog_logout_error_title),
                                content = ResourceUtil.getString(applicationContext, R.string.dialog_logout_error_content),
                                confirmOnClick = this@ConfigViewModel::onDismissDialog,
                                onDismiss = this@ConfigViewModel::onDismissDialog
                            )
                        )
                    )
                }
            }
        }
    }

    override fun onChangePopupUiState() {
        _configPopupUiState.value = !_configPopupUiState.value
    }

    override fun onChangeConfigDialogUiState(dialogType: ConfigDialog) {
        when(dialogType) {
            ConfigDialog.UserName -> showUserNameDialog()
            ConfigDialog.Dismiss -> {}
        }
    }

    override fun onChangeUserNameEditText(text: String) {
        _userNameEditText.value = text
    }

    private fun modifyUserName(userModel: UserModel) {
        viewModelScope.launch {
            addUserUseCase(userModel)
        }
    }

    override fun setDialogChannel(channel: Channel<DialogUiState>) {
        dialogChannel = channel
    }

    override fun setUserNameState(userModel: StateFlow<UserModel>) {
        viewModelScope.launch {
            userModel.collectLatest { userModel ->
                savedUserModel = userModel
                _userNameEditText.value = userModel.userName
            }
        }
    }

    override fun navigateUi(navigateEffect: NavigateEffect) {
        viewModelScope.launch {
            _navigationState.emit(navigateEffect)
        }
    }

    override fun logout() {
        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    dialogType = AppDialog.DefaultTwoButtonDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.dialog_logout_title),
                        content = ResourceUtil.getString(applicationContext, R.string.dialog_logout_content),
                        confirmOnClick = {
                            viewModelScope.launch {
                                logoutUseCase(logoutChannel)
                            }
                        },
                        cancelOnClick = this@ConfigViewModel::onDismissDialog,
                        onDismiss = this@ConfigViewModel::onDismissDialog
                    )
                )
            )
        }
    }

    private fun showUserNameDialog() {
        onChangePopupUiState()

        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    dialogType = AppDialog.UserNameDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.dialog_modify_user_name_title),
                        userName = userNameEditText,
                        onChangeUserName = this@ConfigViewModel::onChangeUserNameEditText,
                        confirmOnClick = {
                            if(::savedUserModel.isInitialized) {
                                modifyUserName(
                                    userModel = savedUserModel.copy(userName = _userNameEditText.value)
                                )

                                onDismissUserNameDialog()
                            }
                        },
                        cancelOnClick = this@ConfigViewModel::onDismissUserNameDialog,
                        onDismiss = this@ConfigViewModel::onDismissUserNameDialog
                    )
                )
            )
        }
    }

    private fun onDismissDialog() {
        viewModelScope.launch {
            logoutUseCase(logoutChannel)
        }
    }

    private fun onDismissUserNameDialog() {
        viewModelScope.launch {
            dialogChannel.send(DialogUiState.Dismiss)

            _userNameEditText.value = savedUserModel.userName
        }
    }
}