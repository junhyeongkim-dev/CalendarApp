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
import com.android.calendarapp.feature.schedule.domain.usecase.RemoveScheduleUseCase
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SchedulePopupViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val getScheduleGroupListUseCase: GetScheduleGroupListUseCase,
    private val getDayScheduleUseCase: GetDayScheduleUseCase,
    private val removeScheduleUseCase: RemoveScheduleUseCase
) : ViewModel(), ISchedulePopupInput, ISchedulePopupOutput {

    val input: ISchedulePopupInput = this
    val output: ISchedulePopupOutput = this

    private val _scheduleData: MutableMap<Int, Flow<List<ScheduleGroupModel>>> = mutableMapOf()
    override val scheduleData: Map<Int, Flow<List<ScheduleGroupModel>>> = _scheduleData

    private val _scheduleUiState: MutableState<Boolean> = mutableStateOf(false)
    override val scheduleUiState: State<Boolean> = _scheduleUiState

    private val _scheduleText: MutableState<String> = mutableStateOf("")
    override val scheduleText: State<String> = _scheduleText

    private val _selectedCategory: MutableState<String> = mutableStateOf(ResourceUtil.getString(applicationContext, R.string.category_default_text))
    override val selectedCategory: State<String> = _selectedCategory

    private val _scheduleList: MutableStateFlow<List<ScheduleModel>> = MutableStateFlow(emptyList())
    override var scheduleList: StateFlow<List<ScheduleModel>> = _scheduleList

    private var currentRoute: String = ""
    private var dialogChannel: Channel<DialogUiState> = Channel()

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

            _scheduleText.value = ""
            _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_text)
        }

        _scheduleUiState.value = !_scheduleUiState.value
    }

    override fun onChangeScheduleEditText(text: String) {
        _scheduleText.value = text
    }

    override fun onChangeCategory(category: String) {
        if(category.isEmpty()) _selectedCategory.value = ResourceUtil.getString(applicationContext, R.string.category_default_text)
        else _selectedCategory.value = category
    }

    override fun onClickAddSchedule(currentPage: Int, yearMonth: String, day: String, categoryName: String) {
        val scheduleContent = _scheduleText.value

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
        }

        onChangeScheduleState()
    }

    override fun setRefreshDayScheduleFlow(flow: Flow<Pair<String, String>>) {
        viewModelScope.launch {

            flow.collectLatest { (yearMonth, day) ->
                withContext(Dispatchers.IO){
                    getDayScheduleUseCase(yearMonth, day).collectLatest { scheduleList ->
                        _scheduleList.value = scheduleList
                    }
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
                    route = currentRoute,
                    dialogType = AppDialog.ScheduleDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.schedule_modify_dialog_title),
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

    override fun setDialogChannel(channel: Channel<DialogUiState>, currentRoute: String) {
        this.currentRoute = currentRoute
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
                    route = currentRoute,
                    dialogType = AppDialog.DefaultTwoButtonDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.schedule_delete_dialog_title),
                        content = ResourceUtil.getString(applicationContext, R.string.schedule_delete_dialog_content,),
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

        viewModelScope.launch(Dispatchers.IO) {
            addScheduleUseCase(
                schedule.copy(
                    scheduleContent = content,
                    categoryName = category
                )
            )
        }
    }

    private fun deleteSchedule(schedule: ScheduleModel, currentPage: Int, yearMonth: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeScheduleUseCase(schedule)

            getMonthScheduleData(currentPage, yearMonth, true)
        }
    }
}