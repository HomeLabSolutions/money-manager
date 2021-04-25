package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import java.math.BigDecimal

abstract class BaseIncomeExpenseViewModel<T : BaseIncomeExpenseNavigator> : BaseViewModel<T>() {

    protected val categories = MutableLiveData<List<Category>>()
    protected val transactions = MutableLiveData<PagedList<BaseTransaction>>()
    protected val addTransactionEvent = SingleLiveEvent<Any>()

    abstract fun saveTransaction(category: Category, sum: BigDecimal)
    fun openAllCategories() {
        navigator?.openCategoriesScreen()
    }

    fun getCategories(): LiveData<List<Category>> = categories
    fun getTransactions(): LiveData<PagedList<BaseTransaction>> = transactions
    fun getTransactionEvent(): LiveData<Any> = addTransactionEvent
}
