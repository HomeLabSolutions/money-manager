package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RemoveSubCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.launch

class SubcategoryRemoveViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<RemoveSubCategoryDialogNavigator>() {

    fun remove(subcategory: Category) = viewModelScope.launch {
        transactionInteractor.removeAllByCategory(subcategory)
        val parentRemoved = categoryInteractor.deleteSubCategory(subcategory)
        if (parentRemoved) {
            navigator?.closeDialogAndGoToCategory()
        } else {
            navigator?.closeDialog()
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory", "name: " + subcategory.name)
        }
    }

    fun removeFromGroup(subcategory: Category) = viewModelScope.launch {
        val parentRemoved = categoryInteractor.deleteFromGroup(subcategory)
        if (parentRemoved) {
            navigator?.closeDialogAndGoToCategory()
        } else {
            navigator?.closeDialog()
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory_from_group", "name: " + subcategory.name)
        }
    }
}
