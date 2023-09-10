package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.RemoveSubCategoryDialogNavigator
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubcategoryRemoveViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<RemoveSubCategoryDialogNavigator>() {

    private val _subCategory = MutableStateFlow(Category.EMPTY_INCOME)

    init {
        viewModelScope.launch {
            val categoryId: Long = checkNotNull(savedStateHandle["subcategory_id"])
            _subCategory.value = categoryInteractor.getCategoryById(categoryId)
        }
    }

    fun remove() = viewModelScope.launch {
        awaitAll(
            async { regularTransactionInteractor.removeAllByCategory(_subCategory.value) },
            async { transactionInteractor.removeAllByCategory(_subCategory.value) }
        )
        val parentRemoved = categoryInteractor.deleteSubCategory(_subCategory.value)
        withContext(Dispatchers.Main) {
            if (parentRemoved) {
                navigator?.closeDialogAndGoToCategory()
            } else {
                navigator?.closeDialog()
            }
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory", "name: " + _subCategory.value.name)
        }
    }

    fun removeFromGroup() = viewModelScope.launch {
        val parentRemoved = categoryInteractor.deleteFromGroup(_subCategory.value)
        withContext(Dispatchers.Main) {
            if (parentRemoved) {
                navigator?.closeDialogAndGoToCategory()
            } else {
                navigator?.closeDialog()
            }
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory_from_group", "name: " + _subCategory.value.name)
        }
    }
}
