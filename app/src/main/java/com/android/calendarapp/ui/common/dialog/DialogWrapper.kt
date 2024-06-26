package com.android.calendarapp.ui.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.android.calendarapp.R
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.ui.calendar.popup.input.ISchedulePopupInput
import com.android.calendarapp.ui.calendar.popup.output.ISchedulePopupOutput
import com.android.calendarapp.ui.common.dialog.models.DialogButton
import com.android.calendarapp.ui.common.dialog.models.DialogContent
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput

@Composable
fun DialogWrapper(appDialog: AppDialog) {
    when(appDialog) {
        is AppDialog.DefaultOneButtonDialog -> {
            DefaultOneButtonDialog(
                title = appDialog.title,
                contentText = appDialog.content,
                confirmOnClick = appDialog.confirmOnClick,
                onDismiss = appDialog.onDismiss

            )
        }
        is AppDialog.DefaultTwoButtonDialog -> {
            DefaultTwoButtonDialog(
                title = appDialog.title,
                contentText = appDialog.content,
                confirmOnClick = appDialog.confirmOnClick,
                cancelOnClick = appDialog.cancelOnClick,
                onDismiss = appDialog.onDismiss
            )
        }

        is AppDialog.CategoryDialog -> {
            CategoryDialog(
                title = appDialog.title,
                text = appDialog.text,
                isNotExistCategoryState = appDialog.isNotExistCategoryState,
                onChangeText = appDialog.onChangeText,
                confirmOnClick = appDialog.confirmOnClick,
                cancelOnClick = appDialog.cancelOnClick,
                onDismiss = appDialog.onDismiss
            )
        }

        is AppDialog.ScheduleDialog -> {
            ScheduleModifyDialog(
                title = appDialog.title,
                schedule = appDialog.schedule,
                scheduleInput = appDialog.scheduleInput,
                scheduleOutput = appDialog.scheduleOutput,
                categoryInput = appDialog.categoryInput,
                categoryOutput = appDialog.categoryOutput,
                confirmOnClick = appDialog.confirmOnClick,
                cancelOnClick = appDialog.cancelOnClick,
                onDismiss = appDialog.onDismiss
            )
        }

        is AppDialog.UserNameDialog -> {
            ModifyUserNameDialog(
                title = appDialog.title,
                username = appDialog.userName,
                onChangeUserName = appDialog.onChangeUserName,
                confirmOnClick = appDialog.confirmOnClick,
                cancelOnClick = appDialog.cancelOnClick,
                onDismiss = appDialog.onDismiss
            )
        }
    }
}


@Composable
fun DefaultOneButtonDialog(
    title: String,
    contentText: String,
    confirmOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Default(contentText),
        buttonList = listOf(
            DialogButton.Default(
                text = "확인",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun DefaultTwoButtonDialog(
    title: String,
    contentText: String,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Default(contentText),
        buttonList = listOf(
            DialogButton.Default(
                text = "취소",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = cancelOnClick
            ),
            DialogButton.Default(
                text = "확인",
                textColor = Color.White,
                buttonColor = Color(colorResource(id = R.color.naver).value),
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun CategoryDialog(
    title: String,
    text: State<String>,
    isNotExistCategoryState: State<Boolean>,
    onChangeText: (String) -> Unit,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Category(
            text = text,
            isNotExistCategoryState = isNotExistCategoryState,
            onChangeText = onChangeText
        ),
        buttonList = listOf(
            DialogButton.Default(
                text = "취소",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = cancelOnClick
            ),
            DialogButton.Default(
                text = "저장",
                textColor = Color.White,
                buttonColor = Color(colorResource(id = R.color.naver).value),
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun ScheduleModifyDialog(
    title: String,
    schedule: ScheduleModel,
    scheduleInput: ISchedulePopupInput,
    scheduleOutput: ISchedulePopupOutput,
    categoryInput: ICategoryPopupInput,
    categoryOutput: ICategoryPopupOutput,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.Schedule(
            schedule = schedule,
            scheduleInput = scheduleInput,
            scheduleOutput = scheduleOutput,
            categoryInput = categoryInput,
            categoryOutput = categoryOutput
        ),
        buttonList = listOf(
            DialogButton.Default(
                text = "취소",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = cancelOnClick
            ),
            DialogButton.Default(
                text = "수정",
                textColor = Color.White,
                buttonColor = Color(colorResource(id = R.color.naver).value),
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun ModifyUserNameDialog(
    title: String,
    username: State<String>,
    onChangeUserName: (String) -> Unit,
    confirmOnClick: () -> Unit,
    cancelOnClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseDialog(
        title = title,
        dialogContent = DialogContent.UserName(
            userName = username,
            onChangeUserName = onChangeUserName
        ),
        buttonList = listOf(
            DialogButton.Default(
                text = "취소",
                textColor = Color.Black,
                buttonColor = Color.LightGray,
                onClick = cancelOnClick
            ),
            DialogButton.Default(
                text = "수정",
                textColor = Color.White,
                buttonColor = Color(colorResource(id = R.color.naver).value),
                onClick = confirmOnClick
            )
        ),
        onDismiss = onDismiss
    )
}