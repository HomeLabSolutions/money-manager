package com.d9tilov.moneymanager.category.ui.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_WITH_SUM_SCREEN

class CategoryViewModel @AssistedInject constructor(
    private val categoryInteractor: CategoryInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<CategoryNavigator>() {

    init {
        val transactionType =
            savedStateHandle.get<TransactionType>("transactionType") ?: TransactionType.EXPENSE
        viewModelScope.launch {
            categories = categoryInteractor.getAllCategoriesByType(transactionType).asLiveData()
        }
    }

    override fun onCategoryClicked(category: Category) {
        val destination = savedStateHandle.get<CategoryDestination>("destination")
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        } else {
            when (destination) {
                EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(category)
                EDIT_REGULAR_TRANSACTION_SCREEN -> navigator?.backToEditRegularTransactionScreen(category)
                MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
                else -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }

    @AssistedFactory
    interface CategoryViewModelFactory {
        fun create(handle: SavedStateHandle): CategoryViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CategoryViewModelFactory,
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
