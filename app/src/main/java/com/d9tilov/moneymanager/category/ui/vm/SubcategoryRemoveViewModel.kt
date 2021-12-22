package com.d9tilov.moneymanager.category.ui.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.RemoveSubCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubcategoryRemoveViewModel @AssistedInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<RemoveSubCategoryDialogNavigator>() {

    fun remove(subcategory: Category) = viewModelScope.launch(Dispatchers.IO) {
        transactionInteractor.removeAllByCategory(subcategory).collect {
            val parentRemoved = categoryInteractor.deleteSubCategory(subcategory)
            withContext(Dispatchers.Main) {
                if (parentRemoved) {
                    navigator?.closeDialogAndGoToCategory()
                } else {
                    navigator?.closeDialog()
                }
            }
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param("delete_subcategory", "name: " + subcategory.name)
            }
        }
    }

    fun removeFromGroup(subcategory: Category) = viewModelScope.launch(Dispatchers.IO) {
        val parentRemoved = categoryInteractor.deleteFromGroup(subcategory)
        withContext(Dispatchers.Main) {
            if (parentRemoved) {
                navigator?.closeDialogAndGoToCategory()
            } else {
                navigator?.closeDialog()
            }
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param("delete_subcategory_from_group", "name: " + subcategory.name)
        }
    }

    @AssistedFactory
    interface SubcategoryRemoveViewModelFactory {
        fun create(handle: SavedStateHandle): SubcategoryRemoveViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: SubcategoryRemoveViewModelFactory,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return assistedFactory.create(handle) as T
                }
            }
    }
}
