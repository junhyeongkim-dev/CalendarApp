package com.android.calendarapp.ui.common.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.calendarapp.R
import com.android.calendarapp.ui.common.popup.config.ConfigPopup
import com.android.calendarapp.ui.common.dialog.DialogInit
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.config.input.IConfigPopupInput
import com.android.calendarapp.ui.theme.CalendarAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseFullScreen(
    title: String,
    navController: NavHostController,
    isShowBackBtn: Boolean = false,
    isShowMoreBtn: Boolean = false,
    isShowBottomLine: Boolean = false,
    configPopupUiState: Boolean = false,
    configCategoryOnClick: (() -> Unit)? = null,
    configInput: IConfigPopupInput? = null,
    dialogUiState: StateFlow<DialogUiState>,
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
                                start = dimensionResource(id = R.dimen.dimen_header_margin).value.dp
                            )
                    ){
                        val backBtn = painterResource(id = R.drawable.ic_back)
                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterStart)
                                .then(
                                    remember {
                                        Modifier.clickable {
                                            navController.popBackStack()
                                        }
                                    }
                                ),
                            painter = backBtn,
                            contentDescription = "뒤로가기 버튼",
                            tint = Color.Black
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

                if (isShowMoreBtn) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                end = dimensionResource(id = R.dimen.dimen_header_margin).value.dp
                            )
                    ){
                        if(null != configInput) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                            ) {
                                ConfigPopup(
                                    configCategoryOnClick = configCategoryOnClick ?: {},
                                    expandState = configPopupUiState,
                                    configInput = configInput
                                )
                            }
                        }

                        Icon(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterEnd)
                                .then(
                                    remember {
                                        Modifier.clickable {
                                            configInput?.onChangePopupUiState()
                                        }
                                    }
                                ),
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "더보기 버튼",
                            tint = Color.Black
                        )
                    }
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

    DialogInit(
        uiState = dialogUiState,
        currentRoute = navController.currentDestination?.route ?: ""
    )
}

@Preview
@Composable
fun BaseFullScreenPreview() {
    CalendarAppTheme {
        BaseFullScreen(
            title = "로그인",
            rememberNavController(),
            dialogUiState = MutableStateFlow(DialogUiState.Dismiss),
            snackBarHostState = SnackbarHostState(),
            ){}
    }
}