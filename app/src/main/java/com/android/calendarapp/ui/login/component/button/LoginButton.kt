package com.android.calendarapp.ui.login.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.ui.theme.CalendarAppTheme

@Composable
fun NaverButton(
    onclick: () -> Unit
) {
    BaseLoginButton(
        buttonColor = colorResource(R.color.naver),
        textColor = Color.White,
        text = "네이버 로그인",
        image = R.drawable.btn_naver_login,
        onclick = onclick
    )
}

@Composable
fun GuestButton(
    onclick: () -> Unit
) {
    BaseLoginButton(
        buttonColor = Color.White,
        textColor = Color.Black,
        text = "게스트 로그인",
        image = R.drawable.btn_guest_login,
        onclick = onclick
    )
}

@Composable
fun BaseLoginButton(
    buttonColor: Color,
    textColor: Color,
    text: String,
    image: Int,
    onclick: () -> Unit
) {
    OutlinedIconButton(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp),
        onClick = onclick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor,
            contentColor = buttonColor
        ),
        shape = RoundedCornerShape(10),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = image),
                contentDescription = "로그인 버튼 이미지",
                tint = Color.Unspecified
            )

            Text(
                text = text,
                color = textColor,
                fontSize = dimensionResource(id = R.dimen.dimen_login_btn_text).value.sp,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun LoginButtonPreview() {
    CalendarAppTheme {
        Column {
            NaverButton{}

            Spacer(modifier = Modifier.size(20.dp))

            GuestButton{}
        }
    }
}