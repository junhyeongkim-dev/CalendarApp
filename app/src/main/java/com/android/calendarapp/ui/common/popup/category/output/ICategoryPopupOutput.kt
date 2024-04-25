package com.android.calendarapp.ui.common.popup.category.output

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import kotlinx.coroutines.flow.StateFlow

interface ICategoryPopupOutput {

    // 카테고리 리스트
    @Stable
    val categoryList: StateFlow<List<CategoryModel>>

    // 카테고리 다이얼로그 텍스트필드
    val categoryDialogText: State<String>

    // 카테고리 없이 저장하려고 할 때 메시지 노출을 위한 상태
    val isNotExistCategoryState: State<Boolean>

    // 카테고리 드랍다운 표시 상태
    val categoryPopupUiState: State<Boolean>
}