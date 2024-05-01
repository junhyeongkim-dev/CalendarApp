package com.android.calendarapp.ui.category

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import com.android.calendarapp.ui.category.input.ICategoryInput
import com.android.calendarapp.ui.category.output.CategoryEffect
import com.android.calendarapp.ui.category.viewmodel.CategoryViewModel
import com.android.calendarapp.ui.common.component.BaseFullScreen
import com.android.calendarapp.ui.theme.CalendarAppTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

@Composable
fun CategoryScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
) {
    val route = navController.currentDestination?.route ?: ""

    BaseFullScreen(
        title = stringResource(id = R.string.config_category_title),
        navController = navController,
        dialogUiState = categoryViewModel.defaultDialogUiState,
        snackBarHostState = categoryViewModel.snackBarHostState,
        isShowBackBtn = true
    ) { paddingValue ->

        val categoryGroupList = categoryViewModel.categoryGroupList.collectAsStateWithLifecycle().value

        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValue.calculateTopPadding()
            ),
            contentPadding = PaddingValues(
                top = 20.dp,
                start = 15.dp
            )
        ) {
            items(
                count = categoryGroupList.size,
                key = { index ->
                    categoryGroupList[index].seqNo
                }
            ) { index ->
                CategoryItem(
                    route = route,
                    categoryItem = categoryGroupList[index],
                    dropdownState = categoryViewModel.dropdownState,
                    input = categoryViewModel.input
                )
            }

            item(key = 0) {
                Row(
                    modifier = Modifier
                        .padding(
                            top = 5.dp,
                            bottom = 15.dp
                        )
                        .then(
                            remember {
                                Modifier.clickable {
                                    categoryViewModel::showCategoryDialog.invoke(0)
                                }
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "새로 만들기 아이콘",
                        tint = Color(colorResource(id = R.color.naver).value)
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = stringResource(id = R.string.category_dropdown_add_text),
                        fontSize = 20.sp,
                        color = Color(colorResource(id = R.color.naver).value)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    route: String,
    categoryItem: CategoryGroupModel,
    dropdownState: StateFlow<CategoryEffect>,
    input: ICategoryInput
) {
    Row(
        modifier = Modifier
            .padding(
                top = 10.dp,
                end = 10.dp,
                bottom = 15.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Filled.List,
            contentDescription = "",
            tint = Color(colorResource(id = R.color.naver).value)
        )

        Text(
            modifier = Modifier
                .width(0.dp)
                .weight(1f)
                .padding(start = 10.dp),
            text = categoryItem.categoryName,
            color = Color.Black,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = categoryItem.count.toString(),
            color = Color.Gray,
            fontSize = 16.sp
        )

        IconButton(
            modifier = Modifier.size(30.dp),
            onClick = {
                input.onChangeDropDownState(CategoryEffect.Show(categoryItem.seqNo))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "카테고리 관리",
                tint = Color.Gray
            )

            ConfigDropdown(
                seqNo = categoryItem.seqNo,
                route = route,
                state = dropdownState,
                input = input
            )
        }
    }
}

@Composable
private fun ConfigDropdown(
    seqNo: Int,
    route: String,
    state: StateFlow<CategoryEffect>,
    input: ICategoryInput
) {
    val expandedState = state.map{ categoryEffect ->
        when(categoryEffect) {
            CategoryEffect.Dismiss -> false
            is CategoryEffect.Show -> categoryEffect.seqNo == seqNo
        }
    }.collectAsStateWithLifecycle(false).value

    DropdownMenu(
        modifier = Modifier.width(80.dp),
        expanded = expandedState,
        onDismissRequest = {
            input.onChangeDropDownState(CategoryEffect.Dismiss)
        },
        offset = DpOffset(10.dp, 0.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "수정",
                    fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }, onClick = {
                input.showCategoryDialog(seqNo)
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "삭제",
                    fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }, onClick = {
                input.deleteCategory(
                    seqNo = seqNo
                )
            }
        )
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
        /*CategoryItem(
            categoryItem = CategoryGroupModel(
                count = 6,
                categoryName = "카테고리입니다."
            )
        )*/
    }
}