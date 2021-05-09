package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.LiveData
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import java.math.BigDecimal

abstract class BaseIncomeExpenseViewModel<T : BaseIncomeExpenseNavigator> : BaseViewModel<T>() {

    lateinit var categories: LiveData<List<Category>>
    protected val addTransactionEvent = SingleLiveEvent<Any>()

    abstract fun saveTransaction(category: Category, sum: BigDecimal)
    fun openAllCategories() {
        navigator?.openCategoriesScreen()
    }

    fun getTransactionEvent(): LiveData<Any> = addTransactionEvent
}
