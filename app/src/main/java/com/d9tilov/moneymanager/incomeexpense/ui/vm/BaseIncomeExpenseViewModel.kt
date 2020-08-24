package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import java.math.BigDecimal

abstract class BaseIncomeExpenseViewModel<T : BaseIncomeExpenseNavigator> : BaseViewModel<T>() {

    val categories = MutableLiveData<List<Category>>()
    val transactions = MutableLiveData<PagedList<BaseTransaction>>()
    val addTransactionEvent = SingleLiveEvent<Any>()

    abstract fun saveTransaction(category: Category, sum: BigDecimal)
    fun openAllCategories() {
        navigator?.openCategoriesScreen()
    }
}
