package com.android.calendarapp.ui.common.popup.category

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel

@Composable
fun CategoryDropDown(
    dropDownState: Boolean,
    categoryItems: List<CategoryModel>,
    selectedCategory: String,
    onChangeDropDownState: () -> Unit,
    onChangeSelectedCategory: (String) -> Unit,
    onClickAddCategory: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.height(250.dp),
        expanded = dropDownState,
        onDismissRequest = onChangeDropDownState
    ) {
        DropdownMenuItem(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                ),
            text = {
                Text(
                    text = "없음",
                    fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                    color = Color.Gray
                )
            }, onClick = {
                onChangeDropDownState.invoke()
                onChangeSelectedCategory("")
            }
        )

        categoryItems.forEach { categoryItem ->
            key(categoryItem.seqNo) {
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(
                            vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                        ),
                    text = {
                        Text(
                            text = categoryItem.categoryName,
                            fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                            color =
                            if(categoryItem.categoryName == selectedCategory) {
                                Color(
                                colorResource(id = R.color.naver).value)
                            } else Color.Black
                        )
                    }, onClick = {
                        onChangeDropDownState.invoke()
                        onChangeSelectedCategory(categoryItem.categoryName)
                    }
                )
            }
        }

        DropdownMenuItem(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.dimen_category_dropdown_item_margin).value.dp
                ),
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "새로 만들기 아이콘",
                        tint = Color(colorResource(id = R.color.naver).value)
                    )
                    Text(
                        text = stringResource(id = R.string.category_dropdown_add_text),
                        fontSize = dimensionResource(id = R.dimen.dimen_category_dropdown_item_text).value.sp,
                        color = Color(colorResource(id = R.color.naver).value)
                    )
                }

            }, onClick = {
                onChangeDropDownState.invoke()
                onClickAddCategory.invoke()
            }
        )
    }
}