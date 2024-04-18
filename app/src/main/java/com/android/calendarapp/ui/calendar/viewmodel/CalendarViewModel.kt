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
    private val tinkHelper: TinkHelper,
    private val naverLoginManager: NaverLoginManager
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

    
    override fun getMonthData(defaultPage: Int, page: Int) {
        val date = if(defaultPage == page){
            // 첫 페이지일 때 현재 년월

            DateUtil.getCurrentYearMonth()
        } else{
            // 해당 페이지에 맞는 년월

            DateUtil.convertPageToYearMonth(defaultPage, page)
        }

        viewModelScope.launch(Dispatchers.Default) {

            if(calendarData.contains(page)) {
                //중복 요청으로 리턴

                return@launch
            }

            try {
                if(date.contains(dateDelimiter)) {

                    val weekData: MutableList<DayItemData> = mutableListOf()
                    val monthData: MutableList<List<DayItemData>> = mutableListOf()

                    val requestYear = date.split(dateDelimiter)[0].toInt()
                    val requestMonth = date.split(dateDelimiter)[1].toInt()

                    // 현재 달의 데이터 요청 시 현재 일 표시를 위한 day 설정
                    val toDay =
                        if (DateUtil.getCurrentYearMonth() == date) DateUtil.getCurrentDay() else ""


                    // 해당 년. 월의 날짜 수
                    val monthsDayCount = DateUtil.getLastDayOfMonth(requestYear, requestMonth)

                    // 시작 요일
                    val firstDayOfWeek = DateUtil.getDayOfWeek(requestYear, requestMonth, 1)

                    // 마지막 요일
                    val lastDayOfWeek = DateUtil.getDayOfWeek(requestYear, requestMonth, monthsDayCount)

                    // 이전 달 추가 일 수
                    val previousCount = DayOfWeekType.entries.indexOf(DayOfWeekType.valueOf(firstDayOfWeek))

                    // 다음 달 추가 일 수
                    val nextCount = 6 - DayOfWeekType.entries.indexOf(DayOfWeekType.valueOf(lastDayOfWeek))

                    // 유저 생일 조회
                    if(userBirth.isEmpty()) {
                        withContext(Dispatchers.IO) {
                            val userId = preferencesHelper.getUserId()
                            userBirth = tinkHelper.stringDecrypt(appDatabase.userInfoDAO().getUserInfo(userId).userBirth, userId)
                        }
                    }

                    // 달력에 표시될 총 일 수
                    var count = 0

                    // 이전 월 날짜 세팅
                    val previousMonth = DateUtil.getPreviousMonth(requestYear, requestMonth, 1) // 전월의 년,월
                    val lastDay = DateUtil.getLastDayOfMonth(previousMonth.first, previousMonth.second) // 전월의 마지막일
                    for (i in 0 until previousCount) {

                        // 이전 월의 날짜는 횟수가 진행될 수록 앞의 날짜기 때문에 계속 리스트의 첫부분에 추가
                        weekData.add(0,
                            DayItemData(
                                dayText = (lastDay - i).toString(),
                                dayColor = Color.LightGray
                            )
                        )

                        count++
                    }

                    // 현재 월 날짜 세팅
                    for (day in 1.. monthsDayCount) {
                        weekData.add(
                            if (userBirth.isNotEmpty() && requestMonth == userBirth.split(dateDelimiter)[0].toInt()
                                && day == userBirth.split(dateDelimiter)[1].toInt()) {
                                // 유저의 생일일 때

                                DayItemData(
                                    dayText = day.toString(),
                                    needBorder = true,
                                    borderColor = Color.Green
                                )
                            } else if (toDay.isNotEmpty() && toDay.toInt() == day){
                                // 오늘

                                DayItemData(
                                    dayText = day.toString(),
                                    dayColor = Color.White,
                                    needBorder = true,
                                    borderBackground = Color.Green,
                                    borderColor = Color.Unspecified
                                )
                            } else {
                                DayItemData(
                                    dayText = day.toString()
                                )
                            }
                        )

                        count++

                        // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                        if(count % 7 == 0) {

                            if( !(previousCount > 0 && monthData.isEmpty()) ) {
                                // 첫 주에 이전달 데이터가 있는 경우 제외 하고 첫번째 Item은 일요일로 빨간색으로 표시

                                weekData[0].dayColor = Color.Red
                            }

                            // 마지막 Item은 토요일로 파란색으로 표시
                            weekData[weekData.lastIndex].dayColor = Color.Blue

                            // 1주일치 month 에 추가
                            monthData.add(weekData.toList())

                            // 기존 데이터 초기화
                            weekData.clear()
                        }
                    }

                    // 다음 월 날짜 세팅
                    for (day in 1 .. nextCount) {

                        // 이전 월의 날짜는 횟수가 진행될 수록 앞의 날짜기 때문에 계속 리스트의 첫부분에 추가
                        weekData.add(
                            DayItemData(
                                dayText = day.toString(),
                                dayColor = Color.LightGray
                            )
                        )

                        count++

                        // 7개가 되면 1주일의 완성으로 monthData에 넣어준다.
                        if(count % 7 == 0) {

                            // 첫날은 일요일로 빨간색
                            weekData[0].dayColor = Color.Red

                            // 1주일치 month 에 추가
                            monthData.add(weekData.toList())
                        }
                    }

                    //현재 요청일 저장
                    _calendarData[page] = monthData
                }else {
                    _calendarErrorData[page] = ResourceUtil.getString(applicationContext, R.string.calendar_data_parsing_error)
                }
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
}