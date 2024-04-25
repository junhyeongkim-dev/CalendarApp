package com.android.calendarapp.ui.common.popup.category.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.calendarapp.R
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.usecase.AddCategoryUseCase
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.ui.common.dialog.AppDialog
import com.android.calendarapp.ui.common.dialog.DialogUiState
import com.android.calendarapp.ui.common.popup.category.input.ICategoryViewModelInput
import com.android.calendarapp.ui.common.popup.category.output.ICategoryViewModelOutput
import com.android.calendarapp.util.ResourceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val applicationContext: Context,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel(), ICategoryViewModelInput, ICategoryViewModelOutput {

    override var categoryList: Flow<List<CategoryModel>> = emptyFlow()

    private val _categoryDialogText: MutableState<String> = mutableStateOf("")
    override val categoryDialogText: State<String> = _categoryDialogText

    private val _isNotExistCategoryState: MutableState<Boolean> = mutableStateOf(false)
    override val isNotExistCategoryState: State<Boolean> = _isNotExistCategoryState

    private var dialogChannel: Channel<DialogUiState> = Channel()

    init {
        init()
    }
    private fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryList = getCategoryListUseCase()
        }
    }

    override fun showCategoryDialog() {
        viewModelScope.launch {
            dialogChannel.send(
                DialogUiState.Show(
                    AppDialog.CategoryDialog(
                        title = ResourceUtil.getString(applicationContext, R.string.category_dialog_title),
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
                                viewModelScope.launch(Dispatchers.IO) {
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