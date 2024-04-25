package com.android.calendarapp.ui.calendar.popup

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.calendar.output.ICalendarOutput
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.popup.category.CategoryDropDown
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput

@Composable
fun SchedulePopup(
    categoryItems: List<CategoryModel>,
    page: Int,
    categoryPopupUiState: Boolean,
    scheduleInput: ISchedulePopupInput,
    scheduleOutput: ISchedulePopupOutput,
    calendarOutput: ICalendarOutput,
    categoryInput: ICategoryPopupInput,
    snackBarEvent: (String) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scheduleInput.onChangeScheduleState()
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
                        .padding(bottom = 10.dp)
                        .focusRequester(focusRequester),
                    value = scheduleOutput.scheduleText.value,
                    onValueChange = { text ->
                        scheduleInput.onChangeScheduleEditText(text)
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
                        cursorColor = Color.Black,
                        selectionColors = TextSelectionColors(
                            handleColor = Color(colorResource(id = R.color.naver).value), // 물방울 핸들 색상
                            backgroundColor = Color.Black.copy(alpha = 0.5f) // 텍스트 선택 영역 색상
                        )
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
                        dropDownState= categoryPopupUiState,
                        categoryItems = categoryItems,
                        selectedCategory = scheduleOutput.selectedCategory.value,
                        onChangeDropDownState = {
                            categoryInput.onChangeCategoryUiState()
                        },
                        onChangeSelectedCategory = { category ->
                            scheduleInput.onChangeCategory(category)
                        },
                        onClickAddCategory = categoryInput::showCategoryDialog
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                shape = CircleShape
                            )
                            .background(Color(colorResource(id = R.color.gray2).value))
                            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                categoryInput.onChangeCategoryUiState()
                            }
                    ) {
                        Text(
                            text = scheduleOutput.selectedCategory.value,
                            color = Color(
                                if(categoryItems.any { it.categoryName == scheduleOutput.selectedCategory.value }) {
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

                    val scheduleText = remember { scheduleOutput::scheduleText.get() }

                    val snackBarMessage = stringResource(id = R.string.snackbar_not_schedule_content)

                    val selectedYearMonth = remember { calendarOutput.selectedYearMonth.value }
                    val selectedDay = remember { calendarOutput.selectedDay.value }
                    val selectedCategory = remember{ scheduleOutput::selectedCategory.get() }
                    val addScheduleFunction = remember{ scheduleInput::onClickAddSchedule }

                    IconButton(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(),
                        onClick = {

                            if(scheduleText.value.isEmpty()) {
                                // 입력된 일정이 없을 때

                                snackBarEvent.invoke(snackBarMessage)
                            }else{
                                addScheduleFunction.invoke(
                                    page, selectedYearMonth, selectedDay, selectedCategory.value
                                )
                            }
                        },
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