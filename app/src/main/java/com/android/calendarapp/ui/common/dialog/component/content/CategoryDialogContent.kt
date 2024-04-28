package com.android.calendarapp.ui.common.dialog.component.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.ui.theme.CalendarAppTheme

private const val maxLength = 30

@Composable
fun CategoryDialogContent(
    text: State<String>,
    isNotExistCategoryState: State<Boolean>,
    onChangeText: (String) -> Unit
) {
    val textFieldValue = text.value
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(140.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(colorResource(id = R.color.gray2).value)
            )
        ) {
            Box{
                Box {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        value = textFieldValue,
                        textStyle = TextStyle(
                            fontSize = dimensionResource(id = R.dimen.dimen_category_dialog_text).value.sp
                        ),
                        onValueChange = { value ->
                            if(value.length <= maxLength) {
                                onChangeText.invoke(value)
                            }
                        },
                        placeholder = {
                            Text(
                                text = "여기에 입력하세요",
                                color = Color(colorResource(id = R.color.gray5).value),
                                fontSize = dimensionResource(id = R.dimen.dimen_category_dialog_text).value.sp
                            )
                        },
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
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 4.dp, end = 8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = "${textFieldValue.length}/${maxLength}",
                        fontSize = 14.sp,
                        color = if(textFieldValue.length == maxLength) {
                            Color.Red
                        }else {
                            Color(colorResource(id = R.color.gray6).value)
                        }
                    )
                }
            }
        }

        if(text.value.length == maxLength) {
            NotiText(stringResource(id = R.string.category_popup_over_max_length))
        }else if(isNotExistCategoryState.value) {
            NotiText(stringResource(id = R.string.category_popup_not_exist_name))
        }
    }
}

@Composable
private fun NotiText(message: String) {
    Text(
        modifier = Modifier.padding(top = 5.dp),
        text = message,
        fontSize = 12.sp,
        color = Color.Red
    )
}

@Preview
@Composable
fun CategoryDialogContentPreview() {
    CalendarAppTheme {
        CategoryDialogContent(
            text = remember {
                mutableStateOf("asdfsdaf")
            },
            isNotExistCategoryState = remember {
                mutableStateOf(false)
            }
        ) {

        }
    }
}