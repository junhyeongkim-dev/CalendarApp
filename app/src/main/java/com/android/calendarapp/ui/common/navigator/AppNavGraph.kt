package com.android.calendarapp.ui.common.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.calendarapp.ui.login.LoginScreen
import com.android.calendarapp.ui.calendar.CalendarScreen
import com.android.calendarapp.ui.category.CategoryScreen
import com.android.calendarapp.ui.common.navigator.type.NavMembers
import com.android.calendarapp.ui.schedule.ScheduleScreen
import com.android.calendarapp.ui.splash.SplashScreen


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = NavMembers.SPLASH.name) {
        addSplash(navController)
        addLogin(navController)
        addSchedule(navController)
        addCalendar(navController)
        addCategory(navController)
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

fun NavGraphBuilder.addSchedule(navController: NavHostController) {
    composable(route = NavMembers.SCHEDULE.name) {
        ScheduleScreen(navController = navController)
    }
}

fun NavGraphBuilder.addCalendar(navController: NavHostController) {
    composable(route = NavMembers.CALENDAR.name) {
        CalendarScreen(navController = navController)
    }
}

fun NavGraphBuilder.addCategory(navController: NavHostController) {
    composable(route = NavMembers.CATEGORY.name) {
        CategoryScreen(navController = navController)
    }
}