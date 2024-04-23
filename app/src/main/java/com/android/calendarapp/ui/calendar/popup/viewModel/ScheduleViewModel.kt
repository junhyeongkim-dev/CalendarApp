package com.android.calendarapp.ui.calendar.popup.viewModel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.schedule.domain.model.ScheduleGroupModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.feature.schedule.domain.usecase.AddScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetDayScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleGroupListUseCase
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.output.IScheduleViewModelOutput
import com.android.calendarapp.ui.common.viewmodel.BaseViewModel
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val getScheduleGroupListUseCase: GetScheduleGroupListUseCase,
    private val getDayScheduleUseCase: GetDayScheduleUseCase
) : ViewModel(), IScheduleViewModelInput, IScheduleViewModelOutput {

    val input: IScheduleViewModelInput = this
    val output: IScheduleViewModelOutput = this

    private val _scheduleData: MutableMap<Int, Flow<List<ScheduleGroupModel>>> = mutableMapOf()
    override val scheduleData: Map<Int, Flow<List<ScheduleGroupModel>>> = _scheduleData

    private val _scheduleUiState: MutableState<Boolean> = mutableStateOf(false)
    override val scheduleUiState: State<Boolean> = _scheduleUiState

    private val _scheduleEditText: MutableState<String> = mutableStateOf("")
    override val scheduleText: State<String> = _scheduleEditText

    private val _dropDownState: MutableState<Boolean> = mutableStateOf(false)
    override val dropDownState: State<Boolean> = _dropDownState

    private val _selectedCategory: MutableState<String> = mutableStateOf(ResourceUtil.getString(applicationContext, R.string.category_default_text))
    override val selectedCategory: State<String> = _selectedCategory

    private val _scheduleList: MutableStateFlow<List<ScheduleModel>> = MutableStateFlow(emptyList())
    override var scheduleList: StateFlow<List<ScheduleModel>> = _scheduleList

    override fun getMonthScheduleData(page: Int, date: String, isForce: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_scheduleData.contains(page) && !isForce) {
                return@launch
            }

            val data = getScheduleGroupListUseCase(date)
            _scheduleData[page] = flow { emit(data) }
        }
    }

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

    override fun onClickAddSchedule(currentPage: Int, yearMonth: String, day: String, categoryName: String) {
        val scheduleContent = _scheduleEditText.value

        viewModelScope.launch(Dispatchers.IO) {
            addScheduleUseCase(
                ScheduleModel(
                    scheduleYearMonth = yearMonth,
                    scheduleDay = day,
                    scheduleContent = scheduleContent,
                    categoryName = categoryName
                )
            )

            getMonthScheduleData(currentPage, yearMonth, true)
            refreshDaySchedule(yearMonth, day)
        }

        onChangeScheduleState()
    }

    private suspend fun refreshDaySchedule(yearMonth: String, day: String) {
        withContext(Dispatchers.IO){
            _scheduleList.value = getDayScheduleUseCase(yearMonth, day)
        }
    }

    override fun setRefreshDayScheduleFlow(flow: Flow<Pair<String, String>>) {
        viewModelScope.launch {

            flow.collectLatest { (yearMonth, day) ->
                refreshDaySchedule(yearMonth, day)
            }
        }
    }
}