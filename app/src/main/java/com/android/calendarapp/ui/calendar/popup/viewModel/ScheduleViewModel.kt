package com.android.calendarapp.ui.calendar.popup.viewModel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.android.calendarapp.R
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.output.IScheduleViewModelOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val applicationContext: Context
) : ViewModel(), IScheduleViewModelInput, IScheduleViewModelOutput {

    val input: IScheduleViewModelInput = this
    val output: IScheduleViewModelOutput = this

    private val _scheduleUiState: MutableState<Boolean> = mutableStateOf(false)
    override val scheduleUiState: State<Boolean> = _scheduleUiState

    private val _scheduleEditText: MutableState<String> = mutableStateOf("")
    override val scheduleText: State<String> = _scheduleEditText

    private val _dropDownState: MutableState<Boolean> = mutableStateOf(false)
    override val dropDownState: State<Boolean> = _dropDownState

    private val _selectedCategory: MutableState<String> = mutableStateOf(ResourceUtil.getString(applicationContext, R.string.category_default_text))
    override val selectedCategory: State<String> = _selectedCategory



    override fun onChangeScheduleState() {
        if(_scheduleUiState.value) {
            // 팝업 닫기 요청 일 때

            _scheduleEditText.value = ""
            _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_text)
        }

        _scheduleUiState.value = !_scheduleUiState.value
    }

    override fun onChangeScheduleEditText(text: String) {
        _scheduleEditText.value = text
    }

    override fun onChangeDropDownState() {
        _dropDownState.value = !_dropDownState.value
    }

    override fun onChangeCategory(category: String) {
        if(category.isEmpty()) _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_text)
        else _selectedCategory.value = category
    }
}