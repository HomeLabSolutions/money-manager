package com.d9tilov.android.incomeexpense.ui.vm

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.core.utils.MainPriceFieldParser
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.isSameDay
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.incomeexpense_ui.R
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.BaseTransaction
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionHeader
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

data class IncomeExpenseUiState(
    val mode: EditMode = EditMode.KEYBOARD,
    val price: Price = Price.EMPTY,
    val expenseUiState: ExpenseUiState = ExpenseUiState.EMPTY,
    val incomeUiState: IncomeUiState = IncomeUiState.EMPTY,
) {
    companion object {
        val EMPTY = IncomeExpenseUiState()
    }
}

@Stable
data class ExpenseUiState(
    val expenseCategoryList: List<Category> = emptyList(),
    val expenseTransactions: Flow<PagingData<BaseTransaction>> = flowOf(),
    val expenseInfo: ExpenseInfo? = null,
) {
    companion object {
        val EMPTY = ExpenseUiState()
    }
}

@Stable
data class IncomeUiState(
    val incomeCategoryList: List<Category> = emptyList(),
    val incomeTransactions: Flow<PagingData<BaseTransaction>> = flowOf(),
    val incomeInfo: IncomeInfo? = null,
) {
    companion object {
        val EMPTY = IncomeUiState()
    }
}

data class Price(
    val value: String = "0",
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val isApprox: Boolean = false,
) {
    companion object {
        val EMPTY = Price()
    }
}

data class ExpenseInfo(
    val ableToSpendToday: TransactionSpendingTodayPrice = TransactionSpendingTodayPrice.NORMAL(
        Price(
            BigDecimal.ZERO.toString(),
            DEFAULT_CURRENCY_CODE
        )
    ),
    val wasSpendToday: Price = Price.EMPTY,
    val wasSpendInPeriod: Price = Price.EMPTY,
)

data class IncomeInfo(
    val wasEarnedInPeriod: Price? = null,
    val wasEarnedInPeriodApprox: Price? = null,
)

enum class ScreenType {
    EXPENSE,
    INCOME;
}

val screenTypes = ScreenType.entries

fun Int.toScreenType(): ScreenType = ScreenType.entries[this]

enum class EditMode {
    KEYBOARD,
    LIST
}

sealed interface TransactionSpendingTodayPrice {

    data class OVERSPENDING(val trSum: Price) : TransactionSpendingTodayPrice

    data class NORMAL(val trSum: Price) : TransactionSpendingTodayPrice
}

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    billingInteractor: BillingInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeExpenseUiState.EMPTY)
    val uiState = _uiState.asStateFlow()
    private val _mainCurrency = MutableStateFlow(CurrencyMetaData.EMPTY)
    private val _errorMessage = MutableSharedFlow<Int>()
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
            Timber.tag(TAG).d("Unable to update currency: $exception")
        }
        viewModelScope.launch(updateCurrencyExceptionHandler) {
            launch {
                currencyInteractor.getMainCurrencyFlow()
                    .collect { currencyData ->
                        _uiState.update { state ->
                            state.copy(price = Price(KeyPress.Zero.value, currencyData.code))
                        }
                        _mainCurrency.value = currencyData
                    }
            }
            launch {
                getSortedCategories(TransactionType.EXPENSE).collect{ list ->
                    _uiState.update { state ->
                        state.copy(expenseUiState = state.expenseUiState.copy(expenseCategoryList = list))
                    }
                }
            }
            launch {
                getSortedCategories(TransactionType.INCOME).collect{ list ->
                    _uiState.update { state ->
                        state.copy(incomeUiState = state.incomeUiState.copy(incomeCategoryList = list))
                    }
                }
            }
            launch {
                _uiState.update { state ->
                    state.copy(
                        expenseUiState = state.expenseUiState.copy(
                            expenseTransactions = mapWithStickyHeaders(transactionInteractor.getTransactionsByType(TransactionType.EXPENSE))
                        )
                    )
                }
            }
            launch {
                _uiState.update { state ->
                    state.copy(
                        incomeUiState = state.incomeUiState.copy(
                            incomeTransactions = mapWithStickyHeaders(
                                transactionInteractor.getTransactionsByType(
                                    TransactionType.INCOME
                                )
                            )
                        )
                    )
                }
            }
            launch {
                val expenseSpendingExpenseInfoFlow = combine(
                    transactionInteractor.ableToSpendToday(),
                    transactionInteractor.isSpendTodayApprox(TransactionType.EXPENSE),
                    transactionInteractor.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
                    transactionInteractor.isSpendInPeriodApprox(TransactionType.EXPENSE),
                    transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE)
                ) { ableToSpendToday, isSpendTodayApprox, spentTodayApprox, isSpendInPeriodApprox, spentInPeriodApprox ->
                    val currencyCode = _mainCurrency.value.code
                    ExpenseInfo(
                        when (ableToSpendToday) {
                            is TransactionSpendingTodayModel.NORMAL -> TransactionSpendingTodayPrice.NORMAL(
                                Price(ableToSpendToday.trSum.toString(), currencyCode)
                            )

                            is TransactionSpendingTodayModel.OVERSPENDING -> TransactionSpendingTodayPrice.OVERSPENDING(
                                Price(ableToSpendToday.trSum.toString(), currencyCode)
                            )
                        },
                        Price(spentTodayApprox.toString(), currencyCode, isSpendTodayApprox),
                        Price(spentInPeriodApprox.toString(), currencyCode, isSpendInPeriodApprox)
                    )
                }
                combine(
                    billingInteractor.isPremium(),
                    expenseSpendingExpenseInfoFlow
                ) { isPremium, info -> if (isPremium) info else null }
                    .collect { info ->
                        _uiState.update { state ->
                            val expenseUiState = state.expenseUiState
                            state.copy(expenseUiState = expenseUiState.copy(expenseInfo = info))
                        }
                    }
            }
            launch {
                combine(
                    transactionInteractor.isSpendInPeriodApprox(TransactionType.INCOME),
                    transactionInteractor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.INCOME)
                ) { sum, approxSum ->
                    val currencyCode = _mainCurrency.value.code
                    IncomeInfo(
                        Price(sum.toString(), currencyCode),
                        Price(approxSum.toString(), currencyCode),
                    )
                }
                    .collect { info ->
                        val incomeInfo = _uiState.value.incomeUiState
                        _uiState.update { state ->
                            state.copy(
                                incomeUiState = incomeInfo.copy(
                                    incomeInfo = info
                                )
                            )
                        }
                    }
            }
        }
//        for (i in 1..200) {
//            viewModelScope.launch {
//                val category = categoryInteractor.getCategoryById(5)
//                transactionInteractor.addTransaction(
//                    Transaction.EMPTY.copy(
//                        sum = Random.nextInt(0, 100).toBigDecimal(),
//                        category = category,
//                        currencyCode = "THB",
//                        date = currentDate().minus(Random.nextInt(0, 30),  DateTimeUnit.DAY).getStartOfDay(),
//                        type = TransactionType.EXPENSE
//                    )
//                )
//            }
//        }
    }

    private fun getSortedCategories(type: TransactionType): Flow<List<Category>> {
        return categoryInteractor.getGroupedCategoriesByType(type)
            .flowOn(Dispatchers.IO)
            .map { list -> list.sortedByDescending { it.usageCount } }
            .flowOn(Dispatchers.Default)
    }

    fun addNumber(btn: KeyPress) {
        _uiState.update { state ->
            val newPriceStr = MainPriceFieldParser.parse(state.price.value, btn)
            val newPrice = state.price.copy(value = newPriceStr)
            state.copy(price = newPrice)
        }
    }

    fun addTransaction(categoryId: Long) {
        if (_uiState.value.price.value.toBigDecimal().signum() == 0) {
            viewModelScope.launch { _errorMessage.emit(R.string.income_expense_empty_sum_error) }
            return
        }
        updateMode(EditMode.LIST)
        _uiState.value.run {
            viewModelScope.launch {
                val category = categoryInteractor.getCategoryById(categoryId)
                transactionInteractor.addTransaction(
                    Transaction.EMPTY.copy(
                        sum = price.value.toBigDecimal(),
                        category = category,
                        currencyCode = price.currencyCode,
                        date = currentDateTime(),
                        type = category.type
                    )
                )
            }
        }
        _uiState.update { state ->
            val price = state.price.copy(value = BigDecimal.ZERO.toString())
            state.copy(price = price)
        }
    }

    fun updateCurrencyCode(code: String) {
        _uiState.update { state ->
            val price = state.price.copy(currencyCode = code)
            state.copy(price = price)
        }
    }

    fun updateMode(mode: EditMode) {
        _uiState.update { state -> state.copy(mode = mode) }
    }

    fun deleteTransaction(transaction: Transaction) {
        val deleteTransactionExceptionHandler = CoroutineExceptionHandler { _, exception ->
            Timber.tag(TAG).d("Delete: $exception")
        }
        viewModelScope.launch(deleteTransactionExceptionHandler) {
            transactionInteractor.removeTransaction(transaction)
        }
    }

    private fun mapWithStickyHeaders(flow: Flow<PagingData<Transaction>>): Flow<PagingData<BaseTransaction>> {
        return flow.map { pagingData ->
            pagingData.insertSeparators { before: Transaction?, after: Transaction? ->
                if (before == null && after == null) null
                else if (before != null && after == null) null
                else if (before == null && after != null)
                    TransactionHeader(
                        after.date.getEndOfDay(),
                        after.currencyCode
                    )
                else if (before != null && after != null && before.date.isSameDay(after.date)) null
                else if (before != null && after != null && !before.date.isSameDay(after.date))
                    TransactionHeader(
                        after.date.getEndOfDay(),
                        after.currencyCode
                    )
                else null
            }
        }
    }
}
