package com.d9tilov.android.incomeexpense.expense.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.isSameDay
import com.d9tilov.android.incomeexpense.navigation.ExpenseNavigator
import com.d9tilov.android.incomeexpense.ui.vm.BaseIncomeExpenseViewModel
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.BaseTransaction
import com.d9tilov.android.transaction.domain.model.ExpenseInfoUiModel
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionHeader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    billingInteractor: BillingInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseIncomeExpenseViewModel<ExpenseNavigator>() {

    override val transactions: Flow<PagingData<BaseTransaction>> =
        transactionInteractor.getTransactionsByType(TransactionType.EXPENSE)
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
                            after.currencyCode
                        )
                        header
                    } else {
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.IO)

    private val _expenseSpendingInfo: Flow<ExpenseInfoUiModel> = combine(
        transactionInteractor.ableToSpendToday(),
        transactionInteractor.isSpendTodayApprox(TransactionType.EXPENSE),
        transactionInteractor.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
        transactionInteractor.isSpendInPeriodApprox(TransactionType.EXPENSE),
        transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
    ) { ableToSpendToday, isTodayApprox, spentTodayApprox, isSpendInPeriodApprox, spentInPeriodApprox ->
        ExpenseInfoUiModel(
            ableToSpendToday,
            isTodayApprox,
            spentTodayApprox,
            isSpendInPeriodApprox,
            spentInPeriodApprox
        )
    }
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val expenseSpendingInfoPremium: Flow<ExpenseInfoUiModel?> = combine(
        billingInteractor.isPremium(),
        _expenseSpendingInfo
    ) { isPremium, info ->
        if (isPremium) {
            info
        } else {
            null
        }
    }
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    override val categories: StateFlow<List<Category>> =
        categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
            } else {
                navigator?.showEmptySumError()
            }
        }
    }
}
