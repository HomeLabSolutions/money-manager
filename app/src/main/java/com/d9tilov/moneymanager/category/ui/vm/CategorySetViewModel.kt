package com.d9tilov.moneymanager.category.ui.vm

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CategorySetViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CategorySetNavigator>() {

    fun save(@DrawableRes categoryIcon: Int) {
        navigator?.save(categoryIcon)
    }

    @AssistedFactory
    interface CategorySetViewModelFactory {
        fun create(handle: SavedStateHandle): CategorySetViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CategorySetViewModelFactory,
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
