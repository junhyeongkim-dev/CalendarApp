package com.android.calendarapp.ui.calendar.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import com.android.calendarapp.ui.calendar.input.ICalendarViewModelInput
import com.android.calendarapp.ui.calendar.model.DayItemModel
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.calendar.output.ICalendarViewModelOutput
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.output.DialogState
import com.android.calendarapp.ui.common.viewmodel.BaseViewModel
import com.android.calendarapp.util.DateUtil
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val preferencesHelper: ISharedPreferencesHelper,
    private val applicationContext: Context,
    private val tinkHelper: TinkHelper,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel(preferencesHelper), ICalendarViewModelInput, ICalendarViewModelOutput {

    val input: ICalendarViewModelInput = this
    val output: ICalendarViewModelOutput = this

    // 현재 월의 앞뒤로 미리 데이터 세팅할 갯수
    val prepareCount = 3

    // 유저 생일
    private var userBirth = ""

    private val _selectedDay: MutableState<String> = mutableStateOf(DateUtil.getCurrentDay())
    override val selectedDay: State<String> = _selectedDay

    override var categoryList: Flow<List<CategoryModel>> = emptyFlow()

    private val _calendarData: MutableMap<Int, List<List<DayItemModel>>> = mutableMapOf()
    override val calendarData: Map<Int, List<List<DayItemModel>>> = _calendarData

    private val _calendarErrorData: MutableMap<Int, String> = mutableMapOf()
    override val calendarErrorData: Map<Int, String> = _calendarErrorData

    private val _calendarUiState: MutableStateFlow<CalendarUiEffect> = MutableStateFlow(CalendarUiEffect.Loading)
    override val calendarUiState: StateFlow<CalendarUiEffect> = _calendarUiState

    private val _scheduleUiState: MutableState<Boolean> = mutableStateOf(false)
    override val scheduleUiState: State<Boolean> = _scheduleUiState

    private val _yearMonthHeader: MutableState<String> = mutableStateOf(DateUtil.getCurrentYearMonth())
    override val yearMonthHeader: State<String> = _yearMonthHeader

    private val _scheduleEditText: MutableState<String> = mutableStateOf("")
    override val scheduleText: State<String> = _scheduleEditText

    private val _dropDownState: MutableState<Boolean> = mutableStateOf(false)
    override val dropDownState: State<Boolean> = _dropDownState

    private val _selectedCategory: MutableState<String> = mutableStateOf(ResourceUtil.getString(applicationContext, R.string.category_default_text))
    override val selectedCategory: State<String> = _selectedCategory

    private val _categoryDialogState: MutableStateFlow<DialogState> = MutableStateFlow(DialogState.Dismiss)
    override val categoryDialogState: StateFlow<DialogState> = _categoryDialogState

    private val _categoryDialogText: MutableState<String> = mutableStateOf("")
    override val categoryDialogText: State<String> = _categoryDialogText


    suspend fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = preferencesHelper.getUserId()
            userBirth = tinkHelper.stringDecrypt(getUserUseCase(userId).userBirth, userId)

            categoryList = getCategoryListUseCase()
        }

        viewModelScope.launch {
            if(_calendarData.size >= 7) {
                // 이미 초기 세팅이 끝났을 때

                _calendarUiState.emit(CalendarUiEffect.Complete)
            }
        }
    }

    override fun getMonthData(defaultPage: Int, page: Int) {
        viewModelScope.launch(Dispatchers.Default) {

            if(calendarData.contains(page)) {
                //중복 요청으로 리턴

                return@launch
            }

            try {
                val monthData = DateUtil.makeMonthData(applicationContext, defaultPage, page, userBirth)

                //현재 요청일 저장
                if(monthData.size >= 4) _calendarData[page] = monthData else _calendarErrorData[page] = ResourceUtil.getString(applicationContext, R.string.calendar_data_parsing_error)
            }catch (e: Exception) {
                _calendarErrorData[page] = e.toString()
            }finally {
                if(_calendarData.size + _calendarErrorData.size == (prepareCount*2+1)) {
                    // 초기 데이터 세팅이 끝났을 때

                    _calendarUiState.emit(CalendarUiEffect.Complete)
                }
            }
        }
    }

    override fun onChangeCalendarDate(date: String) {
        _yearMonthHeader.value = date
    }

    override fun onClickDayItem(day: String) {
        _selectedDay.value = day
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

    override fun onClickAddCategory() {
        viewModelScope.launch {
            _categoryDialogState.emit(
                DialogState.Show(
                    AppDialog.CategoryDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.category_dialog_title),
                        text = categoryDialogText,
                        onChangeText = { text ->
                            _categoryDialogText.value = text
                        },
                        confirmOnClick = {
                            viewModelScope.launch(Dispatchers.IO) {
                                addCategoryUseCase(CategoryModel(
                                    categoryName = _categoryDialogText.value
                                ))
                                
                                onDismissCategoryDialog()
                            }
                        },
                        cancelOnClick = {
                            _categoryDialogText.value = ""
                            onDismissCategoryDialog()
                        },
                        onDismiss = {
                            _categoryDialogText.value = ""
                            onDismissCategoryDialog()
                        }
                    )
                )
            )
        }
    }

    override fun onDismissCategoryDialog() {
        _categoryDialogState.value = DialogState.Dismiss
    }
}