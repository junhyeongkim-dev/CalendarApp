package com.android.calendarapp.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.calendarapp.ui.common.navigator.type.NavMembers
import com.android.calendarapp.ui.splash.output.LoginStateEffect
import com.android.calendarapp.ui.splash.viewmodel.SplashViewModel


@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.loginState.collect { loginStateEffect ->
            when(loginStateEffect) {
                LoginStateEffect.Login -> {

                    navController.navigate(NavMembers.CALENDAR.name) {
                        popUpTo(navController.currentDestination?.route!!) {
                            inclusive= true
                        }
                    }
                }
                is LoginStateEffect.NotLogin -> {

                    navController.navigate(NavMembers.LOGIN.name) {
                        popUpTo(navController.currentDestination?.route!!) {
                            inclusive= true
                        }
                    }
                }

                LoginStateEffect.Wait -> {}
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.checkIsLogin()
    }
}