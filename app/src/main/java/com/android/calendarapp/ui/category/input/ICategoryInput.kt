package com.android.calendarapp.ui.category.input

import com.android.calendarapp.ui.category.output.CategoryEffect

interface ICategoryInput {

    // 카테고리 수정, 삭제 드랍다운 노출 상태 변경
    fun onChangeDropDownState(categoryEffect: CategoryEffect)

    // 카테고리 수정 또는 등록을 위한 다이얼로그 오픈
    fun showCategoryDialog(route: String, seqNo: Int)

    // 카테고리 삭제
    fun deleteCategory(route: String, seqNo: Int)
}