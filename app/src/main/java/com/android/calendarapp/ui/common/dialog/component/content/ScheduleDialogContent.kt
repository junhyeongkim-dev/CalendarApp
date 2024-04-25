package com.android.calendarapp.ui.common.dialog.component.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.popup.input.IScheduleViewModelInput
import com.android.calendarapp.ui.calendar.popup.output.IScheduleViewModelOutput
import com.android.calendarapp.ui.common.popup.category.CategoryDropDown

@Composable
fun ScheduleDialogContent(
    scheduleModel: ScheduleModel,
    scheduleInput: IScheduleViewModelInput,
    scheduleOutput: IScheduleViewModelOutput,
    categoryItems: List<CategoryModel>,
    onClickAddCategory: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(350.dp)
            .height(140.dp)
            .padding(15.dp)
    ) {

        LaunchedEffect(key1 = true) {
            // 수정할 일정 정보 세팅
            scheduleInput.onChangeScheduleEditText(scheduleModel.scheduleContent)
            scheduleInput.onChangeCategory(scheduleModel.categoryName)
        }

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

            CategoryDropDown(
                dropDownState= scheduleOutput.dropDownState.value,
                categoryItems = categoryItems,
                selectedCategory = scheduleOutput.selectedCategory.value,
                onChangeDropDownState = {
                    scheduleInput.onChangeDropDownState()
                },
                onChangeSelectedCategory = { category ->
                    scheduleInput.onChangeCategory(category)
                },
                onClickAddCategory = onClickAddCategory
            )
        }
    }
}