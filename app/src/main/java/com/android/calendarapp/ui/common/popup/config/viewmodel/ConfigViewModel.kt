package com.android.calendarapp.ui.common.popup.config.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.usecase.RemoveCategoryUseCase
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.config.input.IConfigPopupInput
import com.android.calendarapp.ui.common.popup.config.output.ConfigDialog
import com.android.calendarapp.ui.common.popup.config.output.IConfigPopupOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addUserUseCase: AddUserUseCase,
) : ViewModel(), IConfigPopupInput, IConfigPopupOutput {

    val input: IConfigPopupInput = this

    private val _configPopupUiState: MutableState<Boolean> = mutableStateOf(false)
    override val configPopupUiState: State<Boolean> = _configPopupUiState

    private val _configDialogUiState: MutableStateFlow<ConfigDialog> = MutableStateFlow(ConfigDialog.Dismiss)
    override val configDialogUiState: StateFlow<ConfigDialog> = _configDialogUiState

    private val _userNameEditText: MutableState<String> = mutableStateOf("")
    override val userNameEditText: State<String> = _userNameEditText

    // 현재 저장되어 있는 유저 네임
    private lateinit var savedUserModel: UserModel

    private var currentRoute = ""
    private var dialogChannel: Channel<DialogUiState> = Channel()

    override fun onChangePopupUiState() {
        _configPopupUiState.value = !_configPopupUiState.value
    }

    override fun onChangeConfigDialogUiState(dialogType: ConfigDialog) {
        when(dialogType) {
            ConfigDialog.UserName -> showUserNameDialog()
            ConfigDialog.Dismiss -> {}
        }
    }

    private fun showUserNameDialog() {
        onChangePopupUiState()

        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    route = currentRoute,
                    dialogType = AppDialog.UserNameDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.config_modify_user_name_title),
                        userName = userNameEditText,
                        onChangeUserName = this@ConfigViewModel::onChangeUserNameEditText,
                        confirmOnClick = {
                            if(::savedUserModel.isInitialized) {
                                modifyUserName(
                                    userModel = savedUserModel.copy(userName = _userNameEditText.value)
                                )

                                onDismissConfigDialog()
                            }
                        },
                        cancelOnClick = this@ConfigViewModel::onDismissConfigDialog,
                        onDismiss = this@ConfigViewModel::onDismissConfigDialog
                    )
                )
            )
        }
    }

    private fun onDismissConfigDialog() {
        viewModelScope.launch {
            dialogChannel.send(DialogUiState.Dismiss)

            _userNameEditText.value = savedUserModel.userName
        }
    }

    override fun onChangeUserNameEditText(text: String) {
        _userNameEditText.value = text
    }

    private fun modifyUserName(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addUserUseCase(userModel)
        }
    }

    override fun setDialogChannel(channel: Channel<DialogUiState>, currentRoute: String) {
        this.currentRoute = currentRoute
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
}