package com.android.calendarapp.ui.calendar.popup.config.input

import com.android.calendarapp.feature.user.domain.model.UserModel

interface IConfigViewModelInput {

    // 설정 팝업 노출 상태 변경
    fun onChangePopupUiState()

    // 유저 이름 수정
    fun modifyUserName(name: String, userModel: UserModel)

    // 카테고리 관리
    fun configCategory()
}