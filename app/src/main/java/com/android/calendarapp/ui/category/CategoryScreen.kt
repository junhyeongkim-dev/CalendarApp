package com.android.calendarapp.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.common.popup.category.viewmodel.CategoryPopupViewModel
import com.android.calendarapp.ui.theme.CalendarAppTheme

@Composable
fun CategoryScreen(
    categoryPopupViewModel: CategoryPopupViewModel
) {

}

@Composable
fun ConfigCategoryDialogContent(categoryList: List<CategoryModel>) {
    LazyColumn(
        contentPadding = PaddingValues(10.dp)
    ) {
        items(
            count = categoryList.size,
            key = { index ->
                categoryList[index].seqNo
            }
        ){ index ->
            Row(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        end = 10.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = "",
                    tint = Color(colorResource(id = R.color.naver).value)
                )

                Text(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                        .padding(start = 10.dp),
                    text = categoryList[index].categoryName,
                    color = Color.Black,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "",
                    color = Color.Gray,
                    fontSize = 20.sp
                )

                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "카테고리 관리",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ConfigCategoryDialogContentPreview() {
    CalendarAppTheme {
        ConfigCategoryDialogContent(
            categoryList = listOf(
                CategoryModel(0, "아라라라랄라라라라"),
                CategoryModel(1, "로로로로롤로로로로")
            )
        )
    }
}