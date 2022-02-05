package com.d9tilov.moneymanager.incomeexpense.income.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<IncomeNavigator>() {

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    val earnedInPeriod = transactionInteractor.getSumInFiscalPeriodInUsd(TransactionType.INCOME)
        .flowOn(Dispatchers.IO + updateCurrencyExceptionHandler).asLiveData()
    val earnedInPeriodApprox =
        transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.INCOME)
            .flowOn(Dispatchers.IO + updateCurrencyExceptionHandler).asLiveData()

    override val categories: LiveData<List<Category>> =
        categoryInteractor.getGroupedCategoriesByType(TransactionType.INCOME).asLiveData()

    override fun saveTransaction(category: Category, sum: BigDecimal) {
        viewModelScope.launch(Dispatchers.Main) {
            if (sum.signum() > 0) {
                withContext(Dispatchers.IO) {
                    transactionInteractor.addTransaction(
                        Transaction(
                            type = TransactionType.INCOME,
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
    }

    override val transactions: Flow<PagingData<BaseTransaction>> =
        transactionInteractor.getTransactionsByType(TransactionType.INCOME)
            .map { pagingData ->
                var itemPosition = -1
                var itemHeaderPosition = itemPosition
                pagingData.insertSeparators { before: Transaction?, after: Transaction? ->
                    if (before == null && after == null) {
                        null
                    } else if (before != null && after == null) {
                        null
                    } else if (before == null && after != null) {
                        val header = TransactionHeader(
                            after.date.getEndOfDay(),
                            after.currencyCode
                        )
                        header
                    } else if (before != null && after != null && before.date.isSameDay(after.date)) {
                        null
                    } else if (before != null && after != null && !before.date.isSameDay(after.date)) {
                        val header = TransactionHeader(
                            after.date.getEndOfDay(),
                            after.currencyCode,
                        )
                        header
                    } else null
                }.map { item: BaseTransaction ->
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
}
