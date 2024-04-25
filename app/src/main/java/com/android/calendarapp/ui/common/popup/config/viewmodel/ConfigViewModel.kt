package com.android.calendarapp.ui.common.popup.config.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.feature.category.domain.usecase.RemoveCategoryUseCase
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.ui.common.popup.config.input.IConfigViewModelInput
import com.android.calendarapp.ui.common.popup.config.output.IConfigViewModelOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val removeCategoryUseCase: RemoveCategoryUseCase
) : ViewModel(), IConfigViewModelInput, IConfigViewModelOutput {

    private val _configPopupUiState: MutableState<Boolean> = mutableStateOf(false)
    override val configPopupUiState: State<Boolean> = _configPopupUiState
    override fun onChangePopupUiState() {
        _configPopupUiState.value = !_configPopupUiState.value
    }

    override fun modifyUserName(name: String, userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addUserUseCase(userModel.copy(userName = name))
        }
    }

    override fun configCategory() {

    }
}