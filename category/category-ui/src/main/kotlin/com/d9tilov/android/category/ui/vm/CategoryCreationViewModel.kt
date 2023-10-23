package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryArgs
import com.d9tilov.android.category.ui.navigation.CategoryCreationNavigator
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryInteractor: CategoryInteractor,
    private val billingInteractor: BillingInteractor
) : BaseViewModel<CategoryCreationNavigator>() {

    private val categoryArgs: CategoryArgs.CategoryCreationArgs =
        CategoryArgs.CategoryCreationArgs(savedStateHandle)
    private val categoryId: Long = categoryArgs.categoryId
    private val _category = MutableStateFlow(Category.EMPTY_INCOME)
    val category: StateFlow<Category> = _category

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) { navigator?.showError(exception) }
    }

    var isPremium: Boolean = false
        private set

    init {
        viewModelScope.launch {
            launch { _category.value = categoryInteractor.getCategoryById(categoryId) }
            launch {
                billingInteractor.isPremium()
                    .flowOn(Dispatchers.IO)
                    .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
                    .collect { this@CategoryCreationViewModel.isPremium = it }
            }
        }
    }

    fun save(category: Category) {
        viewModelScope.launch(saveCategoryExceptionHandler) {
            if (categoryId == DEFAULT_DATA_ID) {
                categoryInteractor.create(category)
            } else {
                categoryInteractor.update(category)
            }
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
