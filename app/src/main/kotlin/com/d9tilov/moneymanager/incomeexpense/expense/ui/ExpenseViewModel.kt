package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionHeader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<ExpenseNavigator>() {

    override val transactions: Flow<PagingData<BaseTransaction>> =
        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
            .map { pagingData ->
                var itemPosition = -1
                var itemHeaderPosition = itemPosition
                pagingData.insertSeparators { before: Transaction?, after: Transaction? ->
                    if (before == null && after == null) null
                    else if (before != null && after == null) null
                    else if (before == null && after != null) {
                        val header = TransactionHeader(
                            after.date.getEndOfDay(),
                            after.currencyCode
                        )
                        header
                    } else if (before != null && after != null && before.date.isSameDay(after.date)) null
                    else if (before != null && after != null && !before.date.isSameDay(after.date)) {
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
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.IO)
    val ableToSpendToday = transactionInteractor.ableToSpendToday()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)
    val spentToday = transactionInteractor.getSumTodayInUsd(TransactionType.EXPENSE)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)
    val spentInPeriod = transactionInteractor.getSumInFiscalPeriodInUsd(TransactionType.EXPENSE)
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)
    val spentTodayApprox =
        transactionInteractor.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)
    val spentInPeriodApprox =
        transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)
    override val categories: StateFlow<List<Category>> =
        categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    override fun saveTransaction(category: Category, sum: BigDecimal, currencyCode: String) {
        viewModelScope.launch {
            if (sum.signum() > 0) {
                withContext(Dispatchers.IO) {
                    transactionInteractor.addTransaction(
                        Transaction.EMPTY.copy(
                            type = TransactionType.EXPENSE,
                            category = category,
                            currencyCode = currencyCode,
                            sum = sum,
                            date = currentDateTime()
                        )
                    )
                }
                addTransactionEvent.call()
            } else navigator?.showEmptySumError()
        }
    }
}
