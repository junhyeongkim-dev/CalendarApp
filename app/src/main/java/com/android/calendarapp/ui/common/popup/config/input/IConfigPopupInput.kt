package com.android.calendarapp.ui.common.popup.config.input

import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.config.output.NavigateEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

interface IConfigPopupInput {

    // 설정 팝업 노출 상태 변경
    fun onChangePopupUiState()

    fun onChangeUserNameEditText(text: String)

    // 다이얼로그 상태 전달을 위한 채널
    fun setDialogChannel(channel: Channel<DialogUiState>)

    // 유저 이름을 받기 위한 flow
    fun setUserNameState(userModel: StateFlow<UserModel>)

    // 유저 이름 변경 팝업 오픈
    fun showUserNameDialog()

    // 네비게이션 이동
    fun navigateUi(navigateEffect: NavigateEffect)

    // 로그아웃
    fun logout()
}