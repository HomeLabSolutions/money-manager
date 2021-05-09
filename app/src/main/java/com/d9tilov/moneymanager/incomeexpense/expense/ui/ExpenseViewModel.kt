package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
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

class ExpenseViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<ExpenseNavigator>() {

    private var itemPosition = -1
    private var itemHeaderPosition = itemPosition
    lateinit var transactions: Flow<PagingData<BaseTransaction>>

    init {
        viewModelScope.launch {
            categories =
                categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE).asLiveData()
            transactions =
                transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
                    .map {
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
            resetPositions()
        }
    }

    private fun resetPositions() {
        itemPosition = -1
        itemHeaderPosition = itemPosition
    }

    // fun generateData() {
    //     val numbers = IntArray(6) { it + 1 }.toList()
    //     Observable.fromIterable(numbers)
    //         .flatMapCompletable {
    //             categoryInteractor.getCategoryById((3..8).random().toLong())
    //                 .flatMapCompletable { category ->
    //                     transactionInteractor.addTransaction(
    //                         createTransaction(
    //                             category,
    //                             it
    //                         )
    //                     )
    //                 }
    //         }
    //         .subscribeOn(ioScheduler)
    //         .observeOn(uiScheduler)
    //         .subscribe {}
    //         .addTo(compositeDisposable)
    // }
    //
    // fun clearData() {
    //     transactionInteractor.clearAll()
    //         .subscribeOn(ioScheduler)
    //         .observeOn(uiScheduler)
    //         .subscribe()
    //         .addTo(compositeDisposable)
    // }

    // private fun createTransaction(category: Category, number: Int): Transaction {
    //     val d = Date()
    //     val rand = (number..number + 1).random()
    //     val dateBefore = Date(d.time - rand * 24 * 3600 * 1000)
    //     return Transaction(
    //         type = TransactionType.EXPENSE,
    //         sum = number.toBigDecimal(),
    //         date = dateBefore,
    //         category = category
    //     )
    // }

    override fun saveTransaction(category: Category, sum: BigDecimal) {
        if (sum.signum() > 0) {
            viewModelScope.launch {
                transactionInteractor.addTransaction(
                    Transaction(
                        type = TransactionType.EXPENSE,
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
