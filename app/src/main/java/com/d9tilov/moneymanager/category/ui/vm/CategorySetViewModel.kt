package com.d9tilov.moneymanager.category.ui.vm

import androidx.annotation.DrawableRes
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel

class CategorySetViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CategorySetNavigator>() {

    fun save(@DrawableRes categoryIcon: Int) {
        val category = requireNotNull(savedStateHandle.get<Category>("category")).copy(
            icon = categoryIcon
        )
        navigator?.save(category)
    }
}
