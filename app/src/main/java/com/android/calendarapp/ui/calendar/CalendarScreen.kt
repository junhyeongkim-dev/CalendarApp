package com.android.calendarapp.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.ui.calendar.component.error.CalendarError
import com.android.calendarapp.ui.calendar.component.header.CalendarHeader
import com.android.calendarapp.ui.calendar.component.month.MonthItem
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.calendar.viewmodel.CalendarViewModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.util.DateUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// 초기 페이지
private const val DEFAULT_PAGE = 500

// 마지막 페이지
private const val MAX_PAGE = 1000


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val onClickScope = rememberCoroutineScope()

    // 페이저 초기 페이지 및 총 갯수 설정
    // 3개로 전, 현재, 후 반복을 돌릴 수 없어 1000개를 세팅함.. 방법 필요
    val pagerState = rememberPagerState(
        initialPage = DEFAULT_PAGE
    ) {
        MAX_PAGE
    }

    val pagerUiState by viewModel.calendarUiState.collectAsStateWithLifecycle()
    val headerLiveData by viewModel.headerLiveData.observeAsState()

    val snackBarHostState = SnackbarHostState()

    BaseFullScreen(
        title = stringResource(id = R.string.app_bar_calendar_title_name, "김준형"),
        isShowBackBtn = false,
        dialogState = viewModel.dialogState,
        snackBarHostState = snackBarHostState
    ) { paddingValues ->

        Init(viewModel = viewModel)

        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            CalendarHeader(
                date = headerLiveData!!,
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
                state = pagerState,
                beyondBoundsPageCount = 1,
                verticalAlignment = Alignment.Top
            ) {page ->

                val currentPage = pagerState.currentPage

                // 페이지가 바뀔 때 데이터 미리 조회
                LaunchedEffect(key1 = currentPage) {

                    viewModel.changeCalendarDate(DateUtil.convertPageToYearMonth(DEFAULT_PAGE, currentPage))
                    viewModel.getMonthData(DEFAULT_PAGE, changePage(currentPage, viewModel.prepareCount))
                }

                if(pagerUiState == CalendarUiEffect.Complete) {
                    if(viewModel.calendarData[page] == null) {
                        // 달력 데이터가 없을 때

                        CalendarError(viewModel.calendarErrorData[page] ?: "")
                    }else {
                        MonthItem(monthData = viewModel.calendarData[page] ?: listOf())
                    }
                }
            }
        }
    }
}


// 첫 화면 진입 시 실행
@Composable
fun Init(viewModel: CalendarViewModel) {
    LaunchedEffect(key1 = true) {
        coroutineScope {

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