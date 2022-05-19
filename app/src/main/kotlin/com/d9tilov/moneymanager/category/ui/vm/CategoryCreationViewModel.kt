package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryCreationViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val firebaseAnalytics: FirebaseAnalytics,
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
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                    param("create_category", "name: " + category.name)
                }
            } else categoryInteractor.update(category)
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
