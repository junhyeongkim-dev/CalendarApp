package com.android.calendarapp.ui.common.popup.output

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.ui.common.output.DialogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICategoryViewModelOutput {

    // 카테고리 리스트
    @Stable
    val categoryList: Flow<List<CategoryModel>>

    // 카테고리 추가 다이얼로그 노출 여부
    val categoryDialogState: StateFlow<DialogState>

    // 카테고리 다이얼로그 텍스트필드
    val categoryDialogText: State<String>

    // 카테고리 없이 저장하려고 할 때 메시지 노출을 위한 상태
    val isNotExistCategoryState: State<Boolean>
}