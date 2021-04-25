package com.d9tilov.moneymanager.category.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    protected val expenseCategories = MutableLiveData<List<Category>>()
    private val incomeCategories = MutableLiveData<List<Category>>()

    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
    abstract fun update(name: String)

    fun getExpenseCategories(): LiveData<List<Category>> = expenseCategories
    fun getIncomeCategories(): LiveData<List<Category>> = incomeCategories
}
