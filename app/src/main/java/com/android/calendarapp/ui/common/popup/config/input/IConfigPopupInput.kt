package com.android.calendarapp.ui.common.popup.config.input

import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.config.output.ConfigDialog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

interface IConfigPopupInput {

    // 설정 팝업 노출 상태 변경
    fun onChangePopupUiState()

    // config에서 사용하는 dialog 상태 변경
    fun onChangeConfigDialogUiState(dialogType: ConfigDialog)

    fun onChangeUserNameEditText(text: String)

    // 다이얼로그 상태 전달을 위한 채널
    fun setDialogChannel(channel: Channel<DialogUiState>)

    // 유저 이름을 받기 위한 flow
    fun setUserNameState(userModel: StateFlow<UserModel>)
}