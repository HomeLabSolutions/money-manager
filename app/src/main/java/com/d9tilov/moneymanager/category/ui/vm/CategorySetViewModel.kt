package com.d9tilov.moneymanager.category.ui.vm

import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class CategorySetViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CategorySetNavigator>() {

    fun save(@DrawableRes categoryIcon: Int) {
        navigator?.save(categoryIcon)
    }
}
