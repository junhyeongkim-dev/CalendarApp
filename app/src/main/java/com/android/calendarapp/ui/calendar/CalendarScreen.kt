package com.android.calendarapp.ui.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.calendar.component.error.CalendarError
import com.android.calendarapp.ui.calendar.component.header.CalendarHeader
import com.android.calendarapp.ui.calendar.component.month.MonthItem
import com.android.calendarapp.ui.calendar.input.ICalendarViewModelInput
import com.android.calendarapp.ui.calendar.output.CalendarUiEffect
import com.android.calendarapp.ui.calendar.output.ICalendarViewModelOutput
import com.android.calendarapp.ui.calendar.viewmodel.CalendarViewModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.common.dialog.DialogInit
import com.android.calendarapp.ui.common.output.DialogState
import com.android.calendarapp.ui.theme.CalendarAppTheme
import com.android.calendarapp.util.DateUtil
import kotlinx.coroutines.flow.StateFlow
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
    val pagerState = rememberPagerState(
        initialPage = DEFAULT_PAGE
    ) {
        MAX_PAGE
    }

    val pagerUiState by viewModel.calendarUiState.collectAsStateWithLifecycle()

    val scheduleUiState = viewModel.scheduleUiState.value

    val snackBarHostState = SnackbarHostState()

    Init(viewModel)

    CategoryDialog(
        dialogState = viewModel.categoryDialogState
    ){
        viewModel.onDismissDefaultDialog()
    }

    // 스케줄 등록 팝업 오픈 시 뒤로가기 클릭하면 꺼지도록
    BackHandler(enabled = scheduleUiState) {
        viewModel.onChangeScheduleState()
    }

    BaseFullScreen(
        title = stringResource(id = R.string.app_bar_calendar_title_name, "김준형"),
        isShowBackBtn = false,
        dialogState = viewModel.defaultDialogState,
        onBackPress = { viewModel.onDismissDefaultDialog() },
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
                    date = viewModel.yearMonthHeader.value,
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

                        viewModel.onChangeCalendarDate(DateUtil.convertPageToYearMonth(DEFAULT_PAGE, currentPage))
                        viewModel.getMonthData(DEFAULT_PAGE, changePage(currentPage, viewModel.prepareCount))
                    }

                    if(pagerUiState == CalendarUiEffect.Complete) {
                        if(viewModel.calendarData[page] == null) {
                            // 달력 데이터가 없을 때

                            CalendarError(viewModel.calendarErrorData[page] ?: "")
                        }else {
                            MonthItem(
                                monthData = viewModel.calendarData[page] ?: listOf(),
                                selectedDay = viewModel.selectedDay,
                                dayItemOnclick = { day ->
                                    viewModel.onClickDayItem(day)
                                }
                            )
                        }
                    }
                }

                if(!scheduleUiState) {
                    AddScheduleButton {
                        viewModel.onChangeScheduleState()
                    }
                }
            }

            if(scheduleUiState) {
                AddSchedulePopup(
                    input = viewModel.input,
                    output = viewModel.output,
                    categoryItems = viewModel.categoryList.collectAsStateWithLifecycle(initialValue = emptyList()).value
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

@Composable
private fun CategoryDropDown(
    dropDownState: Boolean,
    categoryItems: List<CategoryModel>,
    onChangeDropDownState: () -> Unit,
    onChangeSelectedCategory: (String) -> Unit,
    onClickAddCategory: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.height(250.dp),
        expanded = dropDownState,
        onDismissRequest = onChangeDropDownState
    ) {
        categoryItems.forEach { categoryModel ->
            DropdownMenuItem(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                    ),
                text = {
                    Text(
                        text = categoryModel.categoryName,
                        fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                        color = Color.Black
                    )
                }, onClick = {
                    onChangeDropDownState.invoke()
                    onChangeSelectedCategory(categoryModel.categoryName)
                }
            )
        }

        DropdownMenuItem(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                ),
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "새로 만들기 아이콘",
                        tint = Color(colorResource(id = R.color.naver).value)
                    )
                    Text(
                        text = stringResource(id = R.string.category_dropdown_add_text),
                        fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                        color = Color(colorResource(id = R.color.naver).value)
                    )
                }

            }, onClick = {
                onChangeDropDownState.invoke()
                onClickAddCategory.invoke()
            }
        )
    }
}

@Composable
private fun AddSchedulePopup(
    input: ICalendarViewModelInput,
    output: ICalendarViewModelOutput,
    categoryItems: List<CategoryModel>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        input.onChangeScheduleState()
                    }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color(colorResource(id = R.color.gray1).value))
                .pointerInput(Unit) {
                    detectTapGestures(
                        // 상위 클릭이벤트 무시
                        onPress = {}
                    )
                },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    value = output.scheduleText.value,
                    onValueChange = { text ->
                        input.onChangeScheduleEditText(text)
                    },
                    placeholder = {
                        Text(
                            text = "여기에 새 작업 입력",
                            fontSize = 18.sp,
                            color = Color(colorResource(id = R.color.gray6).value)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(colorResource(id = R.color.gray2).value),
                        unfocusedContainerColor = Color(colorResource(id = R.color.gray2).value),
                        unfocusedIndicatorColor = Color.Transparent, // 포커스가 없을 때 밑줄 색상을 투명하게 설정
                        focusedIndicatorColor = Color.Transparent, // 포커스가 있을 때 밑줄 색상을 투명하게 설정
                        cursorColor = Color.Black
                    ),
                    maxLines = 1
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CategoryDropDown(
                        dropDownState= output.dropDownState.value,
                        categoryItems = categoryItems,
                        onChangeDropDownState = {
                            input.onChangeDropDownState()
                        },
                        onChangeSelectedCategory = { category ->
                            input.onChangeCategory(category)
                        },
                        onClickAddCategory = {
                            input.onClickAddCategory()
                        }
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                shape = CircleShape
                            )
                            .background(Color(colorResource(id = R.color.gray2).value))
                            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                input.onChangeDropDownState()
                            }
                    ) {
                        Text(
                            text = output.selectedCategory.value,
                            color = Color(
                                if(categoryItems.any { it.categoryName == output.selectedCategory.value }) {
                                    colorResource(id = R.color.naver).value
                                }else {
                                    colorResource(id = R.color.gray5).value
                                }
                            ),
                            fontSize = 12.sp,
                            style = LocalTextStyle.current.copy(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }

                    Spacer(modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                    )

                    IconButton(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(),
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(colorResource(id = R.color.naver).value)
                        )
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 4.dp),
                            imageVector = Icons.Filled.Send,
                            contentDescription = "일정 등록 버튼",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryDialog(
    dialogState: StateFlow<DialogState>,
    onBackPress: () -> Unit
    ) {
    DialogInit(
        uiState = dialogState,
        onBackPress = onBackPress
    )
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