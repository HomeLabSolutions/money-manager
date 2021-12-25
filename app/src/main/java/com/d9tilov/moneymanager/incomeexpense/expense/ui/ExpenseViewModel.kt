package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    regularTransactionInteractor: RegularTransactionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<ExpenseNavigator>() {

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    override val transactions: Flow<PagingData<BaseTransaction>> =
        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
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
            .cachedIn(viewModelScope).flowOn(Dispatchers.IO)
    val ableToSpendToday = transactionInteractor.ableToSpendToday()
        .flowOn(Dispatchers.IO + updateCurrencyExceptionHandler).asLiveData()
    val spentInPeriod = transactionInteractor.getSumInFiscalPeriodInUsd(TransactionType.EXPENSE)
        .flowOn(Dispatchers.IO + updateCurrencyExceptionHandler).asLiveData()
    val spentInPeriodApprox = transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
        .flowOn(Dispatchers.IO + updateCurrencyExceptionHandler).asLiveData()
    val regularTransactions = regularTransactionInteractor.getAll(TransactionType.INCOME)
        .zip(regularTransactionInteractor.getAll(TransactionType.EXPENSE)) { income, expense -> income + expense }
        .asLiveData()
    override val categories: LiveData<List<Category>> =
        categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE).asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) { currencyInteractor.updateCurrencyRates() }
    }

    override fun saveTransaction(category: Category, sum: BigDecimal) {
        if (sum.signum() > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                transactionInteractor.addTransaction(
                    Transaction(
                        type = TransactionType.EXPENSE,
                        sum = sum,
                        category = category
                    )
                )
            }
            addTransactionEvent.call()
        } else {
            navigator?.showEmptySumError()
        }
    }

    // fun generateData() = viewModelScope.launch {
    //     val numbers = IntArray(100) { it + 1 }.toList()
    //     for (i in numbers) {
    //         val category = categoryInteractor.getCategoryById((7..11).random().toLong())
    //         transactionInteractor.addTransaction(
    //             createTransaction(
    //                 category,
    //                 i
    //             )
    //         )
    //     }
    // }
    //
    // private fun createTransaction(category: Category, number: Int): Transaction {
    //     val d = Calendar.getInstance().timeInMillis
    //     val rand = (number..number + 1).random()
    //     val dateBefore = Date(d - rand * 24 * 3600 * 100)
    //     return Transaction(
    //         type = TransactionType.EXPENSE,
    //         sum = number.toBigDecimal(),
    //         date = dateBefore,
    //         category = category
    //     )
    // }
}
