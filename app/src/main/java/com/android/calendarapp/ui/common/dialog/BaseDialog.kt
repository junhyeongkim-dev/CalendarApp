package com.android.calendarapp.ui.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.calendarapp.R
import com.android.calendarapp.ui.common.component.LightGrayLine
import com.android.calendarapp.ui.common.dialog.component.button.DialogButtonWrapper
import com.android.calendarapp.ui.common.dialog.component.DialogContentWrapper
import com.android.calendarapp.ui.common.dialog.models.DialogButton
import com.android.calendarapp.ui.common.dialog.models.DialogContent

@Composable
fun BaseDialog(
    title: String,
    dialogContent: DialogContent,
    buttonList: List<DialogButton>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontSize = dimensionResource(id = R.dimen.dimen_popup_title).value.sp,
                        color = Color.Black
                    )
                }

                LightGrayLine()

                DialogContentWrapper(dialogContent)

                DialogButtonWrapper(buttonList)
            }
        }
    }
}