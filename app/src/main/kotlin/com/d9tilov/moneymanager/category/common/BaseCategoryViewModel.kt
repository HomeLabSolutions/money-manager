package com.d9tilov.moneymanager.category.common

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    abstract val categories: StateFlow<List<Category>>

    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
}
