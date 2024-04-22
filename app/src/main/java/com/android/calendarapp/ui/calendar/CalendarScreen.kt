package com.android.calendarapp.ui.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.ui.calendar.component.error.CalendarError
import com.android.calendarapp.ui.calendar.component.header.CalendarHeader
import com.android.calendarapp.ui.calendar.component.month.MonthItem
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.common.popup.CategoryDialogPopup
import com.android.calendarapp.ui.calendar.popup.SchedulePopup
import com.android.calendarapp.ui.calendar.popup.viewModel.ScheduleViewModel
import com.android.calendarapp.ui.calendar.viewmodel.CalendarViewModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.common.popup.viewmodel.CategoryViewModel
import com.android.calendarapp.ui.theme.CalendarAppTheme
import com.android.calendarapp.util.DateUtil
import kotlinx.coroutines.launch

// 초기 페이지
private const val DEFAULT_PAGE = 500

// 마지막 페이지
private const val MAX_PAGE = 1000


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val scheduleViewModel: ScheduleViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    val onClickScope = rememberCoroutineScope()

    // 페이저 초기 페이지 및 총 갯수 설정
    val pagerState = rememberPagerState(
        initialPage = DEFAULT_PAGE
    ) {
        MAX_PAGE
    }

    val pagerUiState by calendarViewModel.calendarUiState.collectAsStateWithLifecycle()

    val scheduleUiState = scheduleViewModel.scheduleUiState.value

    val snackBarHostState = SnackbarHostState()

    Init(calendarViewModel)

    CategoryDialogPopup(
        dialogState = categoryViewModel.categoryDialogState
    ){
        categoryViewModel.onDismissCategoryDialog()
    }

    // 스케줄 등록 팝업 오픈 시 뒤로가기 클릭하면 꺼지도록
    BackHandler(enabled = scheduleUiState) {
        scheduleViewModel.onChangeScheduleState()
    }

    BaseFullScreen(
        title = stringResource(id = R.string.app_bar_calendar_title_name, "김준형"),
        isShowBackBtn = false,
        dialogState = calendarViewModel.defaultDialogState,
        onBackPress = { calendarViewModel.onDismissDefaultDialog() },
        snackBarHostState = snackBarHostState
    ) { paddingValues ->
        Box {
            Column(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    )
            ) {
                CalendarHeader(
                    date = calendarViewModel.yearMonthHeader.value,
                    previousOnClick = {
                        onClickScope.launch {
                            pagerState.scrollToPage(pagerState.currentPage-1)
                        }
                    },
                    nextOnClick = {
                        onClickScope.launch {
                            pagerState.scrollToPage(pagerState.currentPage+1)
                        }
                    }
                )
                HorizontalPager(
                    modifier = Modifier.background(Color(colorResource(id = R.color.calendar_bottom).value)),
                    state = pagerState,
                    beyondBoundsPageCount = 1,
                    verticalAlignment = Alignment.Top
                ) {page ->

                    val currentPage = pagerState.currentPage

                    // 페이지가 바뀔 때 데이터 미리 조회
                    LaunchedEffect(key1 = currentPage) {

                        calendarViewModel.onChangeCalendarDate(DateUtil.convertPageToYearMonth(DEFAULT_PAGE, currentPage))
                        calendarViewModel.getMonthData(DEFAULT_PAGE, changePage(currentPage, calendarViewModel.prepareCount))
                    }

                    if(pagerUiState == CalendarUiEffect.Complete) {
                        if(calendarViewModel.calendarData[page] == null) {
                            // 달력 데이터가 없을 때

                            CalendarError(calendarViewModel.calendarErrorData[page] ?: "")
                        }else {
                            MonthItem(
                                monthData = calendarViewModel.calendarData[page] ?: listOf(),
                                selectedDay = calendarViewModel.selectedDay,
                                dayItemOnclick = { day ->
                                    calendarViewModel.onClickDayItem(day)
                                }
                            )
                        }
                    }
                }

                if(!scheduleUiState) {
                    AddScheduleButton {
                        scheduleViewModel.onChangeScheduleState()
                    }
                }
            }

            if(scheduleUiState) {
                SchedulePopup(
                    categoryItems = categoryViewModel.categoryList.collectAsStateWithLifecycle(initialValue = emptyList()).value,
                    scheduleInput = scheduleViewModel.input,
                    scheduleOutput = scheduleViewModel.output,
                ) {
                    categoryViewModel.showCategoryDialog()
                }
            }
        }
    }
}

@Composable
private fun AddScheduleButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 30.dp, bottom = 30.dp),
        contentAlignment = Alignment.BottomEnd
    ){
        IconButton(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape),
            onClick = {
                onClick.invoke()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(colorResource(id = R.color.naver).value)
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "일정 추가 버튼",
                tint = Color.White
            )
        }
    }
}

// 첫 화면 진입 시 실행
@Composable
private fun Init(viewModel: CalendarViewModel) {
    LaunchedEffect(key1 = true) {

        viewModel.init()

        // 이번달 월별 일자 데이터 요청
        viewModel.getMonthData(DEFAULT_PAGE, DEFAULT_PAGE)

        for (count in 1..3) {
            // 전, 후로 3개월치 월별 일자 데이터를 조회하여 저장

            // 현재보다 이전 월 데이터 조회
            viewModel.getMonthData(DEFAULT_PAGE, changePage(DEFAULT_PAGE-count, 0))

            // 현재보다 이후 월 데이터 조회
            viewModel.getMonthData(DEFAULT_PAGE, changePage( DEFAULT_PAGE+count, 0))
        }
    }
}

private fun changePage(targetPage: Int, intervalCount: Int) : Int {
    return if(DEFAULT_PAGE > targetPage) {
        // 페이지가 첫페이지보다 이전 페이지 (이전 달)

        targetPage - intervalCount
    }else{
        // 페이지가 첫페이지보다 이후 페이지 (다음 달)

        targetPage + intervalCount
    }
}


@Preview
@Composable
fun ScheduleAddDialogPreview() {
    CalendarAppTheme {
        Column {
            /*AddSchedulePopup("", remember {
                true
            }*/
        }
    }
}