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
import com.android.calendarapp.feature.schedule.domain.usecase.GetCurrentScheduleGroupUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetDayScheduleUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.GetScheduleGroupListUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCase
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulePopupViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val getScheduleGroupListUseCase: GetScheduleGroupListUseCase,
    private val getCurrentScheduleGroupUseCase: GetCurrentScheduleGroupUseCase,
    private val getDayScheduleUseCase: GetDayScheduleUseCase,
    private val removeScheduleUseCase: RemoveScheduleUseCase,
) : ViewModel(), ISchedulePopupInput, ISchedulePopupOutput {

    val input: ISchedulePopupInput = this
    val output: ISchedulePopupOutput = this

    private val _scheduleGroupData: MutableMap<Int, List<ScheduleGroupModel>> = mutableMapOf()
    override val scheduleGroupData: Map<Int, List<ScheduleGroupModel>> = _scheduleGroupData

    private val _scheduleUiState: MutableState<Boolean> = mutableStateOf(false)
    override val scheduleUiState: State<Boolean> = _scheduleUiState

    private val _scheduleText: MutableState<String> = mutableStateOf("")
    override val scheduleText: State<String> = _scheduleText

    private val _selectedCategory: MutableState<String> = mutableStateOf(ResourceUtil.getString(applicationContext, R.string.category_default_selected_text))
    override val selectedCategory: State<String> = _selectedCategory

    private val _scheduleList: MutableStateFlow<List<ScheduleModel>> = MutableStateFlow(emptyList())
    override var scheduleList: StateFlow<List<ScheduleModel>> = _scheduleList

    private var _currentScheduleGroupData: MutableStateFlow<List<ScheduleGroupModel>> = MutableStateFlow(emptyList())
    override val currentScheduleGroupData: StateFlow<List<ScheduleGroupModel>> = _currentScheduleGroupData

    private var dialogChannel: Channel<DialogUiState> = Channel()

    fun setCurrentYearMonth(yearMonthState: StateFlow<String>) {
        viewModelScope.launch {
            yearMonthState.collectLatest { yearMonth ->
                getCurrentScheduleGroupUseCase(yearMonth).collect { currentScheduleList ->
                    _currentScheduleGroupData.value = currentScheduleList
                }
            }
        }
    }

    override fun getMonthScheduleData(page: Int, date: String, isForce: Boolean) {
        viewModelScope.launch {
            if (_scheduleGroupData.contains(page) && !isForce) {
                return@launch
            }

            val data = getScheduleGroupListUseCase(date)
            _scheduleGroupData[page] = data
        }
    }

    override fun onChangeScheduleState() {
        if(_scheduleUiState.value) {
            // 팝업 닫기 요청 일 때

            _scheduleText.value = ""
            _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_selected_text)
        }

        _scheduleUiState.value = !_scheduleUiState.value
    }

    override fun onChangeScheduleEditText(text: String) {
        _scheduleText.value = text
    }

    override fun onChangeCategory(category: String) {
        if(category.isEmpty()) _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_selected_text)
        else _selectedCategory.value = category
    }

    override fun onClickAddSchedule(currentPage: Int, yearMonth: String, day: String, categoryName: String) {
        val scheduleContent = _scheduleText.value

        viewModelScope.launch {
            addScheduleUseCase(
                ScheduleModel(
                    yearMonth = yearMonth,
                    day = day,
                    content = scheduleContent,
                    categoryName = categoryName
                )
            )

            getMonthScheduleData(currentPage, yearMonth, true)
        }

        onChangeScheduleState()
    }

    override fun setRefreshDayScheduleFlow(flow: Flow<Pair<String, String>>) {
        viewModelScope.launch {

            flow.collectLatest { (yearMonth, day) ->
                getDayScheduleUseCase(yearMonth, day).collectLatest { scheduleList ->
                    _scheduleList.value = scheduleList
                }
            }
        }
    }

    override fun modifySchedule(
        seqNo: Int,
        categoryInput: ICategoryPopupInput,
        categoryOutput: ICategoryPopupOutput
    ) {
        viewModelScope.launch {
            val schedule = _scheduleList.value.find { it.seqNo == seqNo }!!

            dialogChannel.send(
                DialogUiState.Show(
                    dialogType = AppDialog.ScheduleDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.dialog_modify_schedule_title),
                        schedule = schedule,
                        scheduleInput = input,
                        scheduleOutput = output,
                        categoryInput = categoryInput,
                        categoryOutput = categoryOutput,
                        confirmOnClick = {
                            modifySchedule(schedule)
                            onDismissDialog()
                        },
                        cancelOnClick = {
                            onDismissDialog()
                        },
                        onDismiss = {
                            onDismissDialog()
                        }
                    )
                )
            )
        }
    }

    private fun onDismissDialog() {
        viewModelScope.launch {
            onChangeCategory("")
            onChangeScheduleEditText("")

            dialogChannel.send(DialogUiState.Dismiss)
        }
    }

    override fun setDialogChannel(channel: Channel<DialogUiState>) {
        dialogChannel = channel
    }

    override fun deleteSchedule(
        seqNo: Int,
        currentPage: Int,
        yearMonth: String
    ) {
        val schedule = _scheduleList.value.find { it.seqNo == seqNo }!!

        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    dialogType = AppDialog.DefaultTwoButtonDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.dialog_delete_schedule_title),
                        content = ResourceUtil.getString(applicationContext, R.string.dialog_delete_schedule_content,),
                        confirmOnClick = {
                            deleteSchedule(schedule, currentPage, yearMonth)
                            onDismissDialog()
                        },
                        cancelOnClick = { onDismissDialog() },
                        onDismiss = { onDismissDialog() }
                    )
                )
            )
        }
    }

    // 스케줄 수정
    private fun modifySchedule(schedule: ScheduleModel) {
        val content = _scheduleText.value
        val category = _selectedCategory.value

        viewModelScope.launch {
            addScheduleUseCase(
                schedule.copy(
                    content = content,
                    categoryName = category
                )
            )
        }
    }

    private fun deleteSchedule(schedule: ScheduleModel, currentPage: Int, yearMonth: String) {
        viewModelScope.launch {
            removeScheduleUseCase(schedule)

            getMonthScheduleData(currentPage, yearMonth, true)
        }
    }
}