package com.d9tilov.moneymanager.incomeexpense.income.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class IncomeViewModel @ViewModelInject constructor(
    categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<IncomeNavigator>() {

    lateinit var transactions: Flow<PagingData<BaseTransaction>>

    init {
        viewModelScope.launch {
            categories =
                categoryInteractor.getGroupedCategoriesByType(TransactionType.INCOME).asLiveData()
            transactions =
                transactionInteractor.getTransactionsByType(TransactionType.INCOME)
                    .map {
                        var itemPosition = -1
                        var itemHeaderPosition = itemPosition
                        it.map { item ->
                            var newItem: BaseTransaction = item
                            if (item is TransactionHeader) {
                                itemPosition++
                                itemHeaderPosition = itemPosition
                                newItem = item.copy(headerPosition = itemHeaderPosition)
                            }
                            if (item is Transaction) {
                                itemPosition++
                                newItem = item.copy(headerPosition = itemHeaderPosition)
                            }
                            newItem
                        }
                    }
                    .cachedIn(viewModelScope)
        }
    }

    override fun saveTransaction(category: Category, sum: BigDecimal) {
        if (sum.signum() > 0) {
            viewModelScope.launch {
                transactionInteractor.addTransaction(
                    Transaction(
                        type = TransactionType.INCOME,
                        sum = sum,
                        category = category
                    )
                )
                addTransactionEvent.call()
            }
        } else {
            navigator?.showEmptySumError()
        }
    }
}
