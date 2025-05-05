package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.ui.navigation.CategoryArgs
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.ItemState
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryCreationUiState(
    val category: Category =
        Category.EMPTY_EXPENSE.copy(
            color = com.d9tilov.android.category.data.impl.R.color.category_blue,
            icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
        ),
    val itemState: ItemState = ItemState.UNKNOWN,
    val isPremium: Boolean = false,
    val type: TransactionType = TransactionType.EXPENSE,
    val saveStatus: Result<Unit>? = null,
) {
    companion object {
        val EMPTY = CategoryCreationUiState()
    }
}

@HiltViewModel
class CategoryCreationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val categoryInteractor: CategoryInteractor,
        private val billingInteractor: BillingInteractor,
    ) : ViewModel() {
        private val categoryArgs: CategoryArgs.CategoryCreationArgs =
            CategoryArgs.CategoryCreationArgs(savedStateHandle)
        private val categoryId: Long = categoryArgs.categoryId
        private val transactionType: TransactionType = categoryArgs.transactionType
        private val _uiState = MutableStateFlow(CategoryCreationUiState.EMPTY)
        val uiState: StateFlow<CategoryCreationUiState> = _uiState
        private val categoryExceptionHandler = CoroutineExceptionHandler { _, _ -> }

        init {
            viewModelScope.launch(categoryExceptionHandler) {
                launch {
                    _uiState.update { state: CategoryCreationUiState ->
                        state.copy(
                            category =
                                when (transactionType) {
                                    TransactionType.EXPENSE ->
                                        Category.EMPTY_EXPENSE.copy(
                                            color = com.d9tilov.android.category.data.impl.R.color.category_blue,
                                            icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
                                        )

                                    TransactionType.INCOME ->
                                        Category.EMPTY_INCOME.copy(
                                            color = com.d9tilov.android.category.data.impl.R.color.category_blue,
                                            icon = com.d9tilov.android.category.data.impl.R.drawable.ic_category_salary,
                                        )
                                },
                            itemState = ItemState.CREATE,
                            type = transactionType,
                        )
                    }
                }
                launch {
                    _uiState.update { state: CategoryCreationUiState ->
                        state.copy(
                            category = categoryInteractor.getCategoryById(categoryId),
                            itemState = ItemState.EDIT,
                        )
                    }
                }
                launch {
                    billingInteractor
                        .isPremium()
                        .flowOn(Dispatchers.IO)
                        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
                        .collect {
                            _uiState.update { state: CategoryCreationUiState -> state.copy(isPremium = it) }
                        }
                }
            }
        }

        fun save() {
            val saveCategoryExceptionHandler =
                CoroutineExceptionHandler { _, ex ->
                    _uiState.update { state -> state.copy(saveStatus = Result.failure(ex)) }
                }
            viewModelScope.launch(saveCategoryExceptionHandler) {
                hideError()
                if (categoryId == NO_ID) {
                    categoryInteractor.create(_uiState.value.category)
                } else {
                    categoryInteractor.update(_uiState.value.category)
                }
                _uiState.update { state -> state.copy(saveStatus = Result.success(Unit)) }
            }
        }

        fun updateCategory(category: Category) {
            _uiState.update { state -> state.copy(category = category) }
        }

        fun hideError() {
            _uiState.update { state -> state.copy(saveStatus = null) }
        }
    }
