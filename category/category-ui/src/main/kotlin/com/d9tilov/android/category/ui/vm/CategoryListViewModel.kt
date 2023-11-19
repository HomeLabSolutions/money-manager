package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.Category.Companion.ALL_ITEMS_ID
import com.d9tilov.android.category.ui.navigation.CategoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(val categories: List<Category> = emptyList()) {
    companion object {
        val EMPTY = CategoryUiState()
    }
}

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryInteractor: CategoryInteractor
) : ViewModel() {

    private val categoryArgs: CategoryArgs.CategoryListArgs = CategoryArgs.CategoryListArgs(savedStateHandle)
    val transactionType = checkNotNull(categoryArgs.transactionType)
    private val _uiState = categoryInteractor.getGroupedCategoriesByType(transactionType)
        .map { list -> CategoryUiState(list.filter { it.id != ALL_ITEMS_ID }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CategoryUiState.EMPTY
        )
    val uiState: StateFlow<CategoryUiState> = _uiState

    fun remove(category: Category) = viewModelScope.launch {
        categoryInteractor.deleteCategory(category)
    }
}