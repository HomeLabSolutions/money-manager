package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class CategoryUiState(val categories: List<Category> = emptyList()) {
    companion object {
        val EMPTY = CategoryUiState()
    }
}

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState.EMPTY)
    val uiState = _uiState

    private val categoryArgs: CategoryArgs = CategoryArgs(savedStateHandle)
    private val transactionType = categoryArgs.transactionType

    init {
    }
}