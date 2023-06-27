package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.ui.navigation.CategoryCreationNavigator
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CategoryCreationViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val billingInteractor: BillingInteractor
) : BaseViewModel<CategoryCreationNavigator>() {

    private val saveCategoryExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) { navigator?.showError(exception) }
    }

    var isPremium: Boolean = false
        private set

    init {
        viewModelScope.launch {
            billingInteractor.isPremium()
                .flowOn(Dispatchers.IO)
                .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
                .collect { this@CategoryCreationViewModel.isPremium = it }
        }
    }

    fun save(category: Category) {
        val receivedCategory = savedStateHandle.get<Category>("category")
        viewModelScope.launch(Dispatchers.IO + saveCategoryExceptionHandler) {
            if (receivedCategory == null || receivedCategory.id == DEFAULT_DATA_ID) {
                categoryInteractor.create(category)
            } else categoryInteractor.update(category)
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
