package com.d9tilov.moneymanager.category.common

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.BaseNavigator
import com.d9tilov.moneymanager.category.data.entities.Category

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    val categories = MutableLiveData<List<Category>>()
    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
    abstract fun onItemSwap(categories: List<Category>)
    abstract fun update(name: String)
}
