package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.d9tilov.moneymanager.base.ui.navigator.BaseIncomeExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.SingleLiveEvent
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal

abstract class BaseIncomeExpenseViewModel<T : BaseIncomeExpenseNavigator> : BaseViewModel<T>() {

    protected val addTransactionEvent = SingleLiveEvent<Void>()

    abstract val categories: StateFlow<List<Category>>
    abstract val transactions: Flow<PagingData<BaseTransaction>>
    abstract fun saveTransaction(category: Category, sum: BigDecimal, currencyCode: String)

    fun openAllCategories() {
        navigator?.openCategoriesScreen()
    }

    fun addTransactionEvent(): LiveData<Void> = addTransactionEvent
}
