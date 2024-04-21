package com.android.calendarapp.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.ui.common.input.IBaseViewModelInput
import com.android.calendarapp.ui.common.output.DialogState
import com.android.calendarapp.ui.common.output.IBaseViewModelOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val preferencesHelper: ISharedPreferencesHelper
) : ViewModel(), IBaseViewModelInput, IBaseViewModelOutput {

    private var _defaultDialogState: MutableStateFlow<DialogState> = MutableStateFlow(DialogState.Dismiss)
    override val defaultDialogState: StateFlow<DialogState> = _defaultDialogState

    // 로그인 여부 확인
    override fun isLogin(): Boolean {
        return preferencesHelper.getUserId().isNotEmpty()
    }

    override fun onDismissDefaultDialog() {
        viewModelScope.launch {
            _defaultDialogState.emit(DialogState.Dismiss)
        }
    }

    override fun showDialogDefault(dialogState: DialogState) {
        viewModelScope.launch {
            _defaultDialogState.emit(dialogState)
        }
    }
}