package com.android.calendarapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.common.navigator.type.NavMembers
import com.android.calendarapp.ui.login.component.button.GuestButton
import com.android.calendarapp.ui.login.component.button.NaverButton
import com.android.calendarapp.ui.login.output.LoginNavigateEffect
import com.android.calendarapp.ui.login.viewmodel.LoginViewModel
import com.android.calendarapp.ui.theme.CalendarAppTheme
import com.android.calendarapp.util.ResourceUtil.Companion.getString

@Composable
fun LoginScreen(
    navController: NavHostController,
    isFailLogin: Boolean,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    BaseFullScreen(
        title = stringResource(id = R.string.app_bar_sign_in_title_name),
        isShowBackBtn = false,
        isShowBottomLine = true,
        dialogState = viewModel.defaultDialogState,
        onBackPress = { viewModel.onDismissDefaultDialog() },
        snackBarHostState = viewModel.snackBarHostState
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding() + 100.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "환영합니다! \n 간단한 캘린더입니다. \n 로그인 방식을 선택해주세요",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NaverButton{

                viewModel.login(LoginType.NAVER, context)
            }

            Spacer(modifier = Modifier.size(20.dp))

            GuestButton{
                viewModel.login(LoginType.GUEST, context)
            }
        }
    }

    ObserveNavigateEffect(navController, viewModel)

    // 메시지가 있다면 스낵바 오픈
    if(isFailLogin) {
        LaunchedEffect(key1 = true) {
            viewModel.showSnackBar(
                getString(
                    context,
                    id = R.string.snackbar_auto_login_error
                )
            )
        }
    }
}

/**
 * 화면 전환 상태 구독
 */
@Composable
private fun ObserveNavigateEffect(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    LaunchedEffect(key1 = true) {
        loginViewModel.loginNavigateEffect.collect { loginNavigateEffect ->
            when(loginNavigateEffect) {
                LoginNavigateEffect.GoMain -> {
                    navController.navigate(NavMembers.CALENDAR.name) {
                        popUpTo(navController.currentDestination?.route!!) {

                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginButtonPreview() {
    CalendarAppTheme {
//        LoginScreen()
    }
}