package com.d9tilov.moneymanager.category.common

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    val categories = MutableLiveData<List<Category>>()
    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
    abstract fun onItemSwap(categories: List<Category>)
    abstract fun update(name: String)
}
