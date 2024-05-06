package com.android.calendarapp.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.schedule.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {

    BaseFullScreen(
        title = stringResource(id = R.string.top_bar_schedule_title),
        navController = navController,
        dialogUiState = scheduleViewModel.defaultDialogUiState,
        snackBarHostState = scheduleViewModel.snackBarHostState
    ) {

    }
}