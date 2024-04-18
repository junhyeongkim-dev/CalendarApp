package com.android.calendarapp.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.calendarapp.ui.common.navigator.type.NavMembers
import com.android.calendarapp.ui.splash.output.LoginStateEffect
import com.android.calendarapp.ui.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.coroutineScope


@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigate: (String) -> Unit
) {
    println("safsadfasd")
    /*LaunchedEffect(key1 = true) {
        navController.navigate("${NavMembers.LOGIN.name}/false")
    }*/
    LaunchedEffect(key1 = true) {
        viewModel.loginState.collect { loginStateEffect ->
            when(loginStateEffect) {
                LoginStateEffect.Login -> {

                    coroutineScope {
//                        navController.popBackStack()
//                        navController.navigate(NavMembers.CALENDAR.name)
//                        navController.navigate(NavMembers.CALENDAR.name)
                        navigate.invoke(NavMembers.CALENDAR.name)
                    }
                }
                is LoginStateEffect.NotLogin -> {

                    navigate.invoke(NavMembers.LOGIN.name)
                }

                LoginStateEffect.Wait -> {}
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.checkValidLogin()
    }
}