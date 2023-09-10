package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.EditCategoryDialogNavigator
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryGroupEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<EditCategoryDialogNavigator>() {

    private val categoryId: Long = checkNotNull(savedStateHandle["category_id"])
    private val _category = MutableStateFlow(Category.EMPTY_INCOME)
    val category: StateFlow<Category> = _category

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) { navigator?.showError(exception) }
    }

    init {
        viewModelScope.launch { _category.value = categoryInteractor.getCategoryById(categoryId) }
    }

    fun save(category: Category) {
        viewModelScope.launch(saveCategoryExceptionHandler) {
            categoryInteractor.update(category)
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
