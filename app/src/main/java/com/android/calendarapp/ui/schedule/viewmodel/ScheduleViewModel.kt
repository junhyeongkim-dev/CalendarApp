package com.android.calendarapp.ui.schedule.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.calendarapp.feature.category.domain.model.CategoryModel
import com.android.calendarapp.feature.category.domain.usecase.GetCategoryListUseCase
import com.android.calendarapp.feature.schedule.domain.model.ScheduleModel
import com.android.calendarapp.feature.schedule.domain.usecase.GetSchedulePagingUseCase
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.GetUserUseCase
import com.android.calendarapp.ui.common.base.viewmodel.BaseViewModel
import com.android.calendarapp.ui.schedule.condition.ScheduleCondition
import com.android.calendarapp.ui.schedule.input.IScheduleInput
import com.android.calendarapp.ui.schedule.output.IScheduleOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    getSchedulePagingUseCase: GetSchedulePagingUseCase,
    getUserUseCase: GetUserUseCase,
    getCategoryListUseCase: GetCategoryListUseCase
) : BaseViewModel(), IScheduleInput, IScheduleOutput {

    private val _schedulePagingData: MutableStateFlow<PagingData<ScheduleModel>> = MutableStateFlow(PagingData.empty())
    override val schedulePagingData: StateFlow<PagingData<ScheduleModel>> = _schedulePagingData

    private val _userInfo: MutableStateFlow<UserModel> = MutableStateFlow(UserModel())
    override val userInfo: StateFlow<UserModel> = _userInfo

    private val _categoryList: MutableStateFlow<List<CategoryModel>> = MutableStateFlow(emptyList())
    override val categoryList: StateFlow<List<CategoryModel>> = _categoryList

    private val _selectedCategory: MutableStateFlow<String> = MutableStateFlow("")
    override val selectedCategory: StateFlow<String> = _selectedCategory

    init {

        // 일정 리스트 조회
        viewModelScope.launch {
            _selectedCategory.collectLatest { categoryName ->
                getSchedulePagingUseCase(
                    ScheduleCondition(
                        categoryName = categoryName
                    )
                ).cachedIn(viewModelScope).collect { schedulePagingData ->
                    _schedulePagingData.value = schedulePagingData
                }
            }
        }

        // 유저 정보 조회
        viewModelScope.launch {
            getUserUseCase().collect { userModel ->
                _userInfo.value = userModel
            }
        }

        // 카테고리 리스트 조회
        viewModelScope.launch {
            getCategoryListUseCase().collectLatest { categoryList ->
                _categoryList.value = categoryList
            }
        }
    }

    override fun onChangeSelectedCategory(seqNo: Int) {
        val categoryName = _categoryList.value.find { it.seqNo == seqNo }?.categoryName ?: ""
        _selectedCategory.value = categoryName
    }
}