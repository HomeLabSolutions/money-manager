package com.d9tilov.android.category.ui.vm

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryArgs
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.model.ItemState
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryCreationUiState(
    val category: Category = Category.EMPTY_EXPENSE.copy(
        color = com.d9tilov.android.category_data_impl.R.color.category_blue,
        icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe
    ),
    val itemState: ItemState = ItemState.UNKNOWN,
    val isPremium: Boolean = false,
    val type: TransactionType = TransactionType.EXPENSE,
    val error: ErrorUiState? = null,
) {
    companion object {
        val EMPTY = CategoryCreationUiState()
    }
}

data class ErrorUiState(@StringRes val errorMessageId: Int = R.string.category_unit_name_exist_error)

@HiltViewModel
class CategoryCreationViewModel @Inject constructor(
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


    init {
        val categoryExceptionHandler = CoroutineExceptionHandler { _, _ ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.update { state: CategoryCreationUiState ->
                    state.copy(
                        category = when (transactionType) {
                            TransactionType.EXPENSE -> Category.EMPTY_EXPENSE.copy(
                                color = com.d9tilov.android.category_data_impl.R.color.category_blue,
                                icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe
                            )

                            TransactionType.INCOME -> Category.EMPTY_INCOME.copy(
                                color = com.d9tilov.android.category_data_impl.R.color.category_blue,
                                icon = com.d9tilov.android.category_data_impl.R.drawable.ic_category_salary
                            )
                        },
                        itemState = ItemState.CREATE,
                        type = transactionType
                    )
                }
            }
        }
        viewModelScope.launch(categoryExceptionHandler) {
            launch {
                _uiState.update { state: CategoryCreationUiState ->
                    state.copy(
                        category = categoryInteractor.getCategoryById(categoryId),
                        itemState = ItemState.EDIT
                    )
                }
            }
            launch {
                billingInteractor.isPremium().flowOn(Dispatchers.IO)
                    .shareIn(viewModelScope, SharingStarted.WhileSubscribed()).collect {
                        _uiState.update { state: CategoryCreationUiState -> state.copy(isPremium = it) }
                    }
            }
        }
    }

    fun save(category: Category) {
        val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch(Dispatchers.Main) { }
        }
        viewModelScope.launch(saveCategoryExceptionHandler) {
            if (categoryId == DEFAULT_DATA_ID) {
                categoryInteractor.create(category)
            } else {
                categoryInteractor.update(category)
            }
        }
    }

    fun updateCategory(category: Category) {
        _uiState.update { state -> state.copy(category = category) }
    }

    fun hideError() {
        _uiState.update { state -> state.copy(error = null) }
    }
}
