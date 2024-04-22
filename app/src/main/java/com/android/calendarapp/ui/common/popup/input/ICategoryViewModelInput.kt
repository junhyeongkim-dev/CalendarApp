package com.android.calendarapp.ui.common.popup.input

interface ICategoryViewModelInput {

    // 카테고리 추가 클릭
    fun showCategoryDialog()

    // 카테고리 등록 다이얼로그 닫기
    fun onDismissCategoryDialog()
}