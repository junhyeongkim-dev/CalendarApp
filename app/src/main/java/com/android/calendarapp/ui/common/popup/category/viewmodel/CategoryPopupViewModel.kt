package com.android.calendarapp.ui.common.popup.category.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.category.input.ICategoryPopupInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryPopupOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryPopupViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseViewModel(), ICategoryPopupInput, ICategoryPopupOutput {

    val input: ICategoryPopupInput = this
    val output: ICategoryPopupOutput = this

    private val _categoryList: MutableStateFlow<List<CategoryModel>> = MutableStateFlow(emptyList())
    override var categoryList: StateFlow<List<CategoryModel>> = _categoryList

    private val _categoryDialogText: MutableState<String> = mutableStateOf("")
    override val categoryDialogText: State<String> = _categoryDialogText

    private val _isNotExistCategoryState: MutableState<Boolean> = mutableStateOf(false)
    override val isNotExistCategoryState: State<Boolean> = _isNotExistCategoryState

    private val _categoryPopupUiState: MutableState<Boolean> = mutableStateOf(false)
    override val categoryPopupUiState: State<Boolean> = _categoryPopupUiState

    private var dialogChannel: Channel<DialogUiState> = Channel()

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            getCategoryListUseCase().collect { categoryList ->
                _categoryList.value = categoryList
            }
        }
    }

    override fun onChangeCategoryUiState() {
        _categoryPopupUiState.value = !_categoryPopupUiState.value
    }

    override fun showCategoryDialog() {
        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    dialogType = AppDialog.CategoryDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.dialog_add_category_title),
                        text = categoryDialogText,
                        isNotExistCategoryState = isNotExistCategoryState,
                        onChangeText = { text ->
                            _isNotExistCategoryState.value = false

                            _categoryDialogText.value = text
                        },
                        confirmOnClick = {
                            if(_categoryDialogText.value.isEmpty()) {
                                // 입력된 카테고리 이름이 없을 때

                                _isNotExistCategoryState.value = true
                            } else {
                                viewModelScope.launch {
                                    addCategoryUseCase(
                                        CategoryModel(
                                            categoryName = _categoryDialogText.value
                                        )
                                    )

                                    onDismissCategoryDialog()
                                }
                            }
                        },
                        cancelOnClick = {
                            onDismissCategoryDialog()
                        },
                        onDismiss = {
                            onDismissCategoryDialog()
                        }
                    )
                )
            )
        }
    }

    override fun setDialogChannel(channel: Channel<DialogUiState>) {
        dialogChannel = channel
    }

    private fun onDismissCategoryDialog() {
        viewModelScope.launch {
            _categoryDialogText.value = ""
            dialogChannel.send(DialogUiState.Dismiss)
        }
    }
}