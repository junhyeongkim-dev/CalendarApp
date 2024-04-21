package com.android.calendarapp.ui.common.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.ui.common.dialog.DialogInit
import com.android.calendarapp.ui.common.output.DialogState
import com.android.calendarapp.ui.theme.CalendarAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseFullScreen(
    title: String,
    isShowBackBtn: Boolean,
    isShowBottomLine: Boolean = false,
    dialogState: StateFlow<DialogState>,
    onBackPress: () -> Unit,
    snackBarHostState: SnackbarHostState,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                if (isShowBackBtn) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = dimensionResource(id = R.dimen.dimen_header_start_margin).value.dp
                            )
                    ){
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.CenterStart),
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "뒤로가기 버튼",
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = title,
                        fontSize = dimensionResource(id = R.dimen.dimen_app_bar_title).value.sp,
                        color = Color.Black
                    )
                }

                if(isShowBottomLine) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.LightGray)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackbarData ->
                    // Snackbar 커스터마이징
                    Snackbar(
                        modifier = Modifier.padding(bottom = 20.dp),
                        snackbarData = snackbarData,
                        containerColor = Color.LightGray,
                        contentColor = Color.Black // 텍스트 색상 설정
                    )
                }
            )
        }
    ) {
        content.invoke(it)
    }

    /*DialogInit(
        uiState = dialogState,
        onBackPress = onBackPress
    )*/
}

@Preview
@Composable
fun BaseFullScreenPreview() {
    CalendarAppTheme {
        BaseFullScreen("로그인",true, true, MutableStateFlow(DialogState.Dismiss),{}, SnackbarHostState(), {})
    }
}