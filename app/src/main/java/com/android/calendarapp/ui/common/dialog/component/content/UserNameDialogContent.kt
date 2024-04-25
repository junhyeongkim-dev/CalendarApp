package com.android.calendarapp.ui.common.dialog.component.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R

@Composable
fun UserNameDialogContent(
    userName: State<String>,
    onChangeUserName: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 15.dp,
                end = 15.dp,
                top = 30.dp,
                bottom = 30.dp
            ),
        value = userName.value,
        onValueChange = onChangeUserName,
        placeholder = {
            Text(
                text = "유저 이름을 입력해주세요.",
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
}