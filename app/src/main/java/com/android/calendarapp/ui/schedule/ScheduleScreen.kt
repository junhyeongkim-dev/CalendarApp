package com.android.calendarapp.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.schedule.viewmodel.ScheduleViewModel
import kotlinx.coroutines.flow.filter


@Composable
fun ScheduleScreen(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {
    BaseFullScreen(
        title = stringResource(id = R.string.top_bar_schedule_title, scheduleViewModel.userInfo.collectAsStateWithLifecycle().value.userName),
        navController = navController,
        isShowBottomLine = true,
        dialogUiState = scheduleViewModel.defaultDialogUiState,
        snackBarHostState = scheduleViewModel.snackBarHostState
    ) { paddingValue ->

        val scheduleScrollState = rememberLazyListState()

        val schedulePagingList = scheduleViewModel.schedulePagingData.collectAsLazyPagingItems()
        val categoryList by scheduleViewModel.categoryList.collectAsStateWithLifecycle()
        val selectedCategory by scheduleViewModel.selectedCategory.collectAsStateWithLifecycle()

        LaunchedEffect(schedulePagingList) {
            snapshotFlow { schedulePagingList.loadState.refresh }
                .filter { state ->
                    state is LoadState.NotLoading
                }
                .collect {
                    scheduleScrollState.scrollToItem(0) // 아이템들이 모두 로드된 후 스크롤을 맨 위로 옮김
                }
        }

        Box(
            modifier = Modifier
                .padding(
                    top = paddingValue.calculateTopPadding(),
                    bottom = paddingValue.calculateBottomPadding()
                )
                .background(colorResource(id = R.color.gray1))
                .fillMaxSize()
        ) {
            if(schedulePagingList.itemCount == 0) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(80.dp),
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "일정이 없을 때 아이콘",
                        tint = Color.Black
                    )

                    Text(
                        text = stringResource(id = R.string.schedule_empty_content),
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }else {
                Column {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 10.dp,
                            top = 6.dp,
                            bottom = 6.dp
                        )
                    ) {
                        item(key = 0) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        shape = CircleShape
                                    )
                                    .background(
                                        if (selectedCategory.isEmpty()) Color(colorResource(id = R.color.naver).value)
                                        else Color(colorResource(id = R.color.gray2).value)
                                    )
                                    .padding(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    )
                                    .then(
                                        remember {
                                            Modifier.clickable {
                                                scheduleViewModel::onChangeSelectedCategory.invoke(0)
                                            }
                                        }
                                    )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.category_item_all_text),
                                    color = if(selectedCategory.isEmpty()) Color.White
                                    else Color.Black,
                                    fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_category).value.sp,
                                    style = LocalTextStyle.current.copy(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.size(10.dp))
                        }

                        items(
                            count = categoryList.size,
                            key = { index ->
                                categoryList[index].seqNo
                            }
                        ) { index ->
                            CategoryItem(
                                categoryModel = categoryList[index],
                                selectedCategory = selectedCategory,
                                onChangeCategory = remember { scheduleViewModel::onChangeSelectedCategory }
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp),
                        state = scheduleScrollState
                    ) {
                        items(
                            count = schedulePagingList.itemCount,
                            key = { index ->
                                schedulePagingList[index]!!.seqNo
                            }
                        ) { index ->

                            Column {
                                if(index == 0 || schedulePagingList[index]!!.yearMonth != schedulePagingList[index-1]!!.yearMonth) {
                                    Text(
                                        modifier = Modifier.padding(
                                            top = 10.dp,
                                            bottom = 10.dp
                                        ),
                                        text = schedulePagingList[index]!!.yearMonth,
                                        color = Color.Black,
                                        fontSize = dimensionResource(id = R.dimen.dimen_schedule_date_header).value.sp,
                                    )
                                }

                                ScheduleItem(schedulePagingList[index]!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 스케줄 페이징 item
 */
@Composable
fun ScheduleItem(scheduleModel: ScheduleModel) {

    Card(
        modifier = Modifier.padding(
            bottom = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(
                start = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            )
        ) {
            Text(
                text = "${scheduleModel.day}일",
                fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_category).value.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (scheduleModel.categoryName.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = CircleShape
                            )
                            .background(Color(colorResource(id = R.color.naver).value))
                            .padding(
                                start = 6.dp,
                                end = 6.dp,
                                top = 4.dp,
                                bottom = 4.dp
                            )
                    ) {
                        Text(
                            text = scheduleModel.categoryName,
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_category).value.sp,
                            style = LocalTextStyle.current.copy(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))
                }

                Text(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .padding(end = 20.dp),
                    text = scheduleModel.content,
                    color = Color.Black,
                    fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_content).value.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 카테고리 리스트 item
 */
@Composable
fun CategoryItem(
    categoryModel: CategoryModel,
    selectedCategory: String,
    onChangeCategory: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(
                shape = CircleShape
            )
            .background(
                if (selectedCategory == categoryModel.categoryName) Color(colorResource(id = R.color.naver).value)
                else Color(colorResource(id = R.color.gray2).value)
            )
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .then(
                remember {
                    Modifier.clickable {
                        onChangeCategory.invoke(categoryModel.seqNo)
                    }
                }
            )
    ) {
        Text(
            text = categoryModel.categoryName,
            color = if(selectedCategory == categoryModel.categoryName) Color.White
                    else Color.Black,
            fontSize = dimensionResource(id = R.dimen.dimen_schedule_item_category).value.sp,
            style = LocalTextStyle.current.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
    
    Spacer(modifier = Modifier.size(10.dp))
}