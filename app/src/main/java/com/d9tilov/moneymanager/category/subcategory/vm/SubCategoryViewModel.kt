package com.d9tilov.moneymanager.category.subcategory.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_WITH_SUM_SCREEN
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SubCategoryViewModel @AssistedInject constructor(
    categoryInteractor: CategoryInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    init {
        val parentCategory = savedStateHandle.get<Category>("parent_category")
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        viewModelScope.launch {
            categories = categoryInteractor.getChildrenByParent(parentCategory).asLiveData()
        }
    }

    override fun onCategoryClicked(category: Category) {
        when (savedStateHandle.get<CategoryDestination>("destination")) {
            EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(category)
            MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
            else -> navigator?.openCreateCategoryScreen(category)
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }

    @AssistedFactory
    interface SubCategoryViewModelFactory {
        fun create(handle: SavedStateHandle): SubCategoryViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: SubCategoryViewModelFactory,
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
