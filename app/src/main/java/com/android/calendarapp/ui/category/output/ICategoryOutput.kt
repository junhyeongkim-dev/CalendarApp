package com.android.calendarapp.ui.category.output

import androidx.compose.runtime.State
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import kotlinx.coroutines.flow.StateFlow

interface ICategoryOutput {

    // 카테고리 별 사용숫자 리스트
    val categoryGroupList: StateFlow<List<CategoryGroupModel>>

    // 수정 또는 삭제 드랍다운 노출 상태값
    val dropdownState: StateFlow<CategoryEffect>

    // 카테고리 다이얼로그 텍스트필드
    val categoryDialogText: State<String>

    // 카테고리 없이 저장하려고 할 때 메시지 노출을 위한 상태
    val isNotExistCategoryState: State<Boolean>
}

sealed class CategoryEffect {
    data object Dismiss : CategoryEffect()
    data class Show(
        val seqNo: Int
    ) : CategoryEffect()
}