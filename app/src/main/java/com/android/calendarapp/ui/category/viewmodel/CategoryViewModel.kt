package com.android.calendarapp.ui.category.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryGroupModel
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryGroupListUseCase
import com.android.calendarapp.feature.category.domain.usecase.RemoveCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.UpdateCategoryUseCase
import com.android.calendarapp.feature.schedule.domain.usecase.UpdateScheduleUseCase
import com.android.calendarapp.ui.category.input.ICategoryInput
import com.android.calendarapp.ui.category.output.CategoryEffect
import com.android.calendarapp.ui.category.output.ICategoryOutput
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val applicationContext: Context,
    private val categoryGroupListUseCase: GetCategoryGroupListUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val removeCategoryUseCase: RemoveCategoryUseCase
) : BaseViewModel(), ICategoryInput, ICategoryOutput {

    val input: ICategoryInput = this

    private val _categoryGroupList: MutableStateFlow<List<CategoryGroupModel>> = MutableStateFlow( emptyList() )
    override val categoryGroupList: StateFlow<List<CategoryGroupModel>> = _categoryGroupList

    private val _dropdownState: MutableStateFlow<CategoryEffect> = MutableStateFlow(CategoryEffect.Dismiss)
    override val dropdownState: StateFlow<CategoryEffect> = _dropdownState

    private val _categoryDialogText: MutableState<String> = mutableStateOf("")
    override val categoryDialogText: State<String> = _categoryDialogText

    private val _isNotExistCategoryState: MutableState<Boolean> = mutableStateOf(false)
    override val isNotExistCategoryState: State<Boolean> = _isNotExistCategoryState

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryGroupListUseCase().collect { categoryGroupList ->
                _categoryGroupList.value = categoryGroupList
            }
        }
    }

    override fun onChangeDropDownState(categoryEffect: CategoryEffect) {
        viewModelScope.launch {
            _dropdownState.emit(categoryEffect)
        }
    }

    override fun showCategoryDialog(route: String, seqNo: Int) {
        _dropdownState.value = CategoryEffect.Dismiss

        val targetCategoryGroup = _categoryGroupList.value.find{ it.seqNo == seqNo } ?: CategoryGroupModel()

        _categoryDialogText.value = targetCategoryGroup.categoryName

        showDialog(
            DialogUiState.Show(
                route = route,
                dialogType = AppDialog.CategoryDialog(
                    title = ResourceUtil.getString(
                        context = applicationContext,
                        id = if(seqNo != 0) R.string.category_modify_dialog_title else R.string.category_add_dialog_title
                    ),
                    text = categoryDialogText,
                    isNotExistCategoryState = isNotExistCategoryState,
                    onChangeText = { text ->
                        _isNotExistCategoryState.value = false

                        _categoryDialogText.value = text
                    },
                    confirmOnClick = {
                        if(_categoryDialogText.value.isEmpty()) {
                            _isNotExistCategoryState.value = true
                        }else {
                            if(seqNo != 0) modifyCategory(targetCategoryGroup)
                            else addCategory(
                                CategoryModel(categoryName = _categoryDialogText.value)
                            )

                            onDismissDialog()
                        }
                    },
                    onDismiss = {
                        onDismissDialog()
                    },
                    cancelOnClick = {
                        onDismissDialog()
                    }
                )
            )
        )
    }

    override fun deleteCategory(route: String, seqNo: Int) {
        _dropdownState.value = CategoryEffect.Dismiss

        val categoryGroup = categoryGroupList.value.find { it.seqNo == seqNo } ?: CategoryGroupModel()

        showDialog(
            DialogUiState.Show(
                route = route,
                dialogType = AppDialog.DefaultTwoButtonDialog(
                    title = ResourceUtil.getString(applicationContext, R.string.category_delete_dialog_title),
                    content = ResourceUtil.getString(applicationContext, R.string.category_delete_dialog_content,),
                    confirmOnClick = {
                        deleteCategory(categoryGroup)
                        onDismissDialog()
                    },
                    cancelOnClick = { onDismissDialog() },
                    onDismiss = { onDismissDialog() }
                )
            )
        )
    }

    private fun addCategory(categoryModel: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryUseCase(categoryModel)
        }
    }

    private fun modifyCategory(targetCategoryGroup: CategoryGroupModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCategoryUseCase(
                targetCategoryGroup.copy(categoryName = _categoryDialogText.value)
            )

            updateScheduleUseCase(
                currentCategoryName = targetCategoryGroup.categoryName,
                changeCategoryName = _categoryDialogText.value
            )
        }
    }

    private fun deleteCategory(categoryGroup: CategoryGroupModel) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCategoryUseCase(
                categoryGroup
            )

            updateScheduleUseCase(
                currentCategoryName = categoryGroup.categoryName,
                changeCategoryName = ""
            )
        }
    }
}