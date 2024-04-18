package com.android.calendarapp.ui.calendar.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.login.naver.manager.NaverLoginManager
import com.android.calendarapp.library.database.AppDatabase
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import com.android.calendarapp.ui.calendar.data.DayItemData
import com.android.calendarapp.ui.calendar.input.ICalendarViewModelInput
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.calendar.output.ICalendarViewModelOutput
import com.android.calendarapp.ui.calendar.type.DayOfWeekType
import com.android.calendarapp.ui.common.viewmodel.BaseViewModel
import com.android.calendarapp.util.DateUtil
import com.android.calendarapp.util.ResourceUtil
import com.android.calendarapp.util.dateDelimiter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val preferencesHelper: ISharedPreferencesHelper,
    private val appDatabase: AppDatabase,
    private val applicationContext: Context,
    private val tinkHelper: TinkHelper
) : BaseViewModel(preferencesHelper), ICalendarViewModelInput, ICalendarViewModelOutput {

    // 현재 월의 앞뒤로 미리 데이터 세팅할 갯수
    val prepareCount = 3

    // 유저 생일
    private var userBirth = ""

    private val _calendarData: MutableMap<Int, List<List<DayItemData>>> = mutableMapOf()
    override val calendarData: Map<Int, List<List<DayItemData>>> = _calendarData

    private val _calendarErrorData: MutableMap<Int, String> = mutableMapOf()
    override val calendarErrorData: Map<Int, String> = _calendarErrorData

    private val _calendarUiState: MutableStateFlow<CalendarUiEffect> = MutableStateFlow(CalendarUiEffect.Loading)
    override val calendarUiState: StateFlow<CalendarUiEffect> = _calendarUiState

    private val _headerLiveData: MutableLiveData<String> = MutableLiveData(DateUtil.getCurrentYearMonth())
    override val headerLiveData: LiveData<String> = _headerLiveData

    private val _selectedDay: MutableLiveData<String> = MutableLiveData(DateUtil.getCurrentDay())
    override val selectedDay: LiveData<String> = _selectedDay

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val userId = preferencesHelper.getUserId()
            userBirth = tinkHelper.stringDecrypt(appDatabase.userInfoDAO().getUserInfo(userId).userBirth, userId)
        }
    }

    override fun getMonthData(defaultPage: Int, page: Int) {
        viewModelScope.launch(Dispatchers.Default) {

            if(calendarData.contains(page)) {
                //중복 요청으로 리턴

                return@launch
            }

            try {
                val monthData = DateUtil.makeMonthData(defaultPage, page, userBirth)

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

    override fun changeCalendarDate(date: String) {
        _headerLiveData.value = date
    }

    override fun clickDayItem(day: String) {
        _selectedDay.value = day
    }
}