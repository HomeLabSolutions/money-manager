package com.d9tilov.moneymanager.category.common

import androidx.lifecycle.LiveData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    lateinit var categories: LiveData<List<Category>>

    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
}
