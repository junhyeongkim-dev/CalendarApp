package com.android.calendarapp.ui.common.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.calendarapp.ui.login.LoginScreen
import com.android.calendarapp.ui.calendar.CalendarScreen
import com.android.calendarapp.ui.common.navigator.type.NavMembers
import com.android.calendarapp.ui.splash.SplashScreen


@Composable
fun AppNavigator(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = NavMembers.SPLASH.name) {
        addSplash(navController)
        addLogin(navController)
        addMain(navController)
    }
}

fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(route = NavMembers.SPLASH.name) {
        SplashScreen(navController)
    }
}

fun NavGraphBuilder.addLogin(navController: NavHostController) {
    composable(route = "${NavMembers.LOGIN.name}?isFailLogin = {isFailLogin}") {
        LoginScreen(navController = navController, isFailLogin = (it.arguments?.getString("isFailLogin") ?: "false").toBoolean())
    }
}

fun NavGraphBuilder.addMain(navController: NavHostController) {
    composable(route = NavMembers.CALENDAR.name) {
        CalendarScreen(navController = navController)
    }
}
