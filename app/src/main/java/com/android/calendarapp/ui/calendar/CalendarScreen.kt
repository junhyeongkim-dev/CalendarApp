package com.android.calendarapp.ui.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.component.error.CalendarError
import com.android.calendarapp.ui.calendar.component.header.CalendarHeader
import com.android.calendarapp.ui.calendar.component.month.MonthItem
import com.android.calendarapp.ui.calendar.input.ICalendarViewModelInput
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.calendar.popup.SchedulePopup
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.viewModel.ScheduleViewModel
import com.android.calendarapp.ui.calendar.viewmodel.CalendarViewModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.common.popup.CategoryDialogPopup
import com.android.calendarapp.ui.common.popup.viewmodel.CategoryViewModel
import com.android.calendarapp.ui.theme.CalendarAppTheme
import com.android.calendarapp.util.DateUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
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

    // 페이저에 화면 노출을 위한 데이터 준비 상태
    val pagerUiState by calendarViewModel.calendarUiState.collectAsStateWithLifecycle()

    // 스케줄 등록 팝업 노출 상태
    val scheduleUiState = scheduleViewModel.scheduleUiState.value

    LaunchedEffect(key1 = true) {
        calendarViewModel.init()
        scheduleViewModel.setRefreshDayScheduleFlow(calendarViewModel.refreshDayScheduleFlow)
    }

    Init(calendarViewModel.input, scheduleViewModel.input)

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
        snackBarHostState = calendarViewModel.snackBarHostState
    ) { paddingValues ->
        Box {
            Column(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    )
            ) {
                // 현재 페이저의 년월 및 요일 헤더
                CalendarHeader(
                    date = calendarViewModel.yearMonthHeader.collectAsStateWithLifecycle(initialValue = DateUtil.getCurrentYearMonth()).value,
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

                // 달력 페이저
                HorizontalPager(
                    modifier = Modifier.background(Color.White),
                    state = pagerState,
                    beyondBoundsPageCount = 1,
                    verticalAlignment = Alignment.Top
                ) {page ->

                    // 페이지가 바뀔 때 데이터 미리 조회
                    LaunchedEffect(key1 = pagerState.currentPage) {

                        val changePage = changePage(pagerState.currentPage, calendarViewModel.prepareCount)
                        calendarViewModel.onChangeCalendarDate(DateUtil.convertPageToYearMonth(DEFAULT_PAGE, pagerState.currentPage))
                        calendarViewModel.getMonthData(DEFAULT_PAGE, changePage)
                        scheduleViewModel.getMonthScheduleData(
                            page = changePage,
                            date = DateUtil.convertPageToYearMonth(DEFAULT_PAGE, changePage),
                            isForce = false
                        )
                    }

                    if(pagerUiState == CalendarUiEffect.Complete) {
                        if(calendarViewModel.calendarData[page] == null) {
                            // 달력 데이터가 없을 때

                            CalendarError(calendarViewModel.calendarErrorData[page] ?: "")
                        }else {
                            MonthItem(
                                monthData = calendarViewModel.calendarData[page] ?: listOf(),
                                selectedDay = calendarViewModel.selectedDay.collectAsStateWithLifecycle(initialValue = "").value,
                                scheduleData = scheduleViewModel.scheduleData[page] ?: emptyFlow(),
                                dayItemOnclick = { day ->
                                    calendarViewModel.onClickDayItem(day)
                                }
                            )
                        }
                    }
                }

                // 선택된 일의 일정 리스트 또는 등록 안내 페이지
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.gray1))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val scheduleList = scheduleViewModel.scheduleList.collectAsStateWithLifecycle(initialValue = emptyList()).value

                        if(scheduleList.isNotEmpty()) {
                            Column {
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp))

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        start = 15.dp,
                                        end = 15.dp
                                    )
                                ) {
                                    items(
                                        count = scheduleList.size,
                                        key = { index ->
                                            scheduleList[index].seqNo
                                        }
                                    ) {index ->
                                        ScheduleItem(scheduleList[index])
                                    }
                                }
                            }
                        }else {
                            Text(
                                modifier = Modifier.padding(bottom = 50.dp),
                                text = stringResource(id = R.string.calendar_empty_schedule),
                                color = colorResource(id = R.color.gray4),
                                fontSize = dimensionResource(id = R.dimen.dimen_schedule_empty_text).value.sp,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }

                    if(!scheduleUiState) {
                        AddScheduleButton(
                            onClick = remember {
                                scheduleViewModel::onChangeScheduleState
                            }
                        )
                    }
                }
            }

            if(scheduleUiState) {
                SchedulePopup(
                    categoryItems = categoryViewModel.categoryList.collectAsStateWithLifecycle(initialValue = emptyList()).value,
                    page = remember{ pagerState.currentPage },
                    scheduleInput = scheduleViewModel.input,
                    scheduleOutput = scheduleViewModel.output,
                    calendarOutput = calendarViewModel.output,
                    snackBarEvent = remember { calendarViewModel::showSnackBar },
                    onClickAddCategory = remember { categoryViewModel::showCategoryDialog }
                )
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
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(colorResource(id = R.color.naver).value)
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "스케줄 등록 팝업 오픈 버튼",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ScheduleItem(scheduleItem: ScheduleModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.gray2)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(colorResource(id = R.color.naver))
            )
            Text(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f)
                    .padding(start = 10.dp),
                text = scheduleItem.scheduleContent,
                fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_content).value.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(10.dp))

            if(scheduleItem.categoryName.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = CircleShape
                        )
                        .background(Color(colorResource(id = R.color.naver).value))
                        .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = scheduleItem.categoryName,
                        color = Color.White,
                        fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_category).value.sp,
                        style = LocalTextStyle.current.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
        }
    }
}

// 첫 화면 진입 시 실행
@Composable
private fun Init(calendarInput: ICalendarViewModelInput, scheduleInput: IScheduleViewModelInput) {
    LaunchedEffect(key1 = true) {

        // 이번달 월별 일자 데이터 요청
        calendarInput.getMonthData(DEFAULT_PAGE, DEFAULT_PAGE)
        scheduleInput.getMonthScheduleData(DEFAULT_PAGE, DateUtil.convertPageToYearMonth(DEFAULT_PAGE, DEFAULT_PAGE), false)

        for (count in 1..3) {
            // 전, 후로 3개월치 월별 일자 데이터를 조회하여 저장

            // 현재보다 이전 월 데이터 조회
            calendarInput.getMonthData(DEFAULT_PAGE, changePage(DEFAULT_PAGE-count, 0))
            scheduleInput.getMonthScheduleData(DEFAULT_PAGE-count, DateUtil.convertPageToYearMonth(DEFAULT_PAGE, DEFAULT_PAGE-count), false)


            // 현재보다 이후 월 데이터 조회
            calendarInput.getMonthData(DEFAULT_PAGE, changePage( DEFAULT_PAGE+count, 0))
            scheduleInput.getMonthScheduleData(DEFAULT_PAGE+count, DateUtil.convertPageToYearMonth(DEFAULT_PAGE, DEFAULT_PAGE+count), false)
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