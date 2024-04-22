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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.output.IScheduleViewModelOutput
import com.android.calendarapp.ui.calendar.popup.viewModel.ScheduleViewModel

@Composable
fun SchedulePopup(
    categoryItems: List<CategoryModel>,
    scheduleInput: IScheduleViewModelInput,
    scheduleOutput: IScheduleViewModelOutput,
    onClickAddCategory: () -> Unit
) {
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
                        .padding(bottom = 10.dp),
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
                        dropDownState= scheduleOutput.dropDownState.value,
                        categoryItems = categoryItems,
                        onChangeDropDownState = {
                            scheduleInput.onChangeDropDownState()
                        },
                        onChangeSelectedCategory = { category ->
                            scheduleInput.onChangeCategory(category)
                        },
                        onClickAddCategory = onClickAddCategory
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                shape = CircleShape
                            )
                            .background(Color(colorResource(id = R.color.gray2).value))
                            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                scheduleInput.onChangeDropDownState()
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
        DropdownMenuItem(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                ),
            text = {
                Text(
                    text = "없음",
                    fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                    color = Color.Gray
                )
            }, onClick = {
                onChangeDropDownState.invoke()
                onChangeSelectedCategory("")
            }
        )

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