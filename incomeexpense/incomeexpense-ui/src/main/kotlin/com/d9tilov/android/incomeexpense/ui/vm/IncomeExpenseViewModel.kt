package com.d9tilov.android.incomeexpense.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.core.utils.MainPriceFieldParser
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.isSameDay
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.BaseTransaction
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionHeader
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

data class IncomeExpenseUiState(
    val screenTypes: List<ScreenType> = ScreenType.values().asList(),
    val editMode: EditMode = EditMode.KEYBOARD,
    val price: Price = Price.EMPTY,
    val expenseUiState: ExpenseUiState = ExpenseUiState.EMPTY,
    val incomeUiState: IncomeUiState = IncomeUiState.EMPTY,
) {
    companion object {
        val EMPTY = IncomeExpenseUiState()
    }
}

data class ExpenseUiState(
    val expenseCategoryList: List<Category> = emptyList(),
    val expenseTransactions: Flow<PagingData<BaseTransaction>> = flowOf(),
    val expenseInfo: ExpenseInfo? = null,
) {
    companion object {
        val EMPTY = ExpenseUiState()
    }
}

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
    val currency: String = DEFAULT_CURRENCY_SYMBOL,
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
            DEFAULT_CURRENCY_SYMBOL
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

fun Int.toScreenType(): ScreenType = ScreenType.values()[this]

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
    private val currencyInteractor: CurrencyInteractor,
    private val categoryInteractor: CategoryInteractor,
    billingInteractor: BillingInteractor,
    transactionInteractor: TransactionInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeExpenseUiState.EMPTY)
    val uiState = _uiState.asStateFlow()
    private val _mainCurrency = MutableStateFlow(CurrencyMetaData.EMPTY)

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    init {
        viewModelScope.launch(updateCurrencyExceptionHandler) {
//            launch {
//                val category = categoryInteractor.getCategoryById(2)
//                for(i in 0..100) {
//                    val day = i / 5
//                    transactionInteractor.addTransaction(
//                        Transaction.EMPTY.copy(
//                            type = TransactionType.EXPENSE,
//                            sum = BigDecimal(Random.nextInt(100)),
//                            category = category,
//                            date = LocalDate(2023,9,29).minus(day, DateTimeUnit.DAY).getStartOfDay(),
//                            currencyCode = DEFAULT_CURRENCY_CODE
//                        )
//                    )
//                }
//            }

            launch {
                currencyInteractor.getMainCurrencyFlow()
                    .collect { currencyData ->
                        _uiState.update { state ->
                            state.copy(price = Price(KeyPress.Zero.value, currencyData.symbol))
                        }
                        _mainCurrency.value = currencyData
                        Timber.tag(TAG).d("launch1")
                    }
            }
            launch {
                categoryInteractor.getAllCategoriesByType(TransactionType.INCOME)
                    .collect { list ->
                        _uiState.update { state ->
                            Timber.tag(TAG).d("launch2: $list")
                            state.copy(
                                incomeUiState = state.incomeUiState.copy(
                                    incomeCategoryList = list,
                                    incomeTransactions = mapWithStickyHeaders(transactionInteractor.getTransactionsByType(TransactionType.INCOME))
                                )
                            )
                        }
                    }
            }
            launch {
                categoryInteractor.getAllCategoriesByType(TransactionType.EXPENSE)
                    .collect { list ->
                        _uiState.update { state ->
                            Timber.tag(TAG).d("launch3: $list")
                            state.copy(
                                expenseUiState = state.expenseUiState.copy(
                                    expenseCategoryList = list,
                                    expenseTransactions = mapWithStickyHeaders(transactionInteractor.getTransactionsByType(TransactionType.EXPENSE))
                                )
                            )
                        }
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
                    val currencySymbol = _mainCurrency.value.symbol
                    ExpenseInfo(
                        when (ableToSpendToday) {
                            is TransactionSpendingTodayModel.NORMAL -> TransactionSpendingTodayPrice.NORMAL(
                                Price(ableToSpendToday.trSum.toString(), currencySymbol)
                            )

                            is TransactionSpendingTodayModel.OVERSPENDING -> TransactionSpendingTodayPrice.OVERSPENDING(
                                Price(ableToSpendToday.trSum.toString(), currencySymbol)
                            )
                        },
                        Price(spentTodayApprox.toString(), currencySymbol, isSpendTodayApprox),
                        Price(spentInPeriodApprox.toString(), currencySymbol, isSpendInPeriodApprox)
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
                    val currencySymbol = _mainCurrency.value.symbol
                    IncomeInfo(
                        Price(sum.toString(), currencySymbol),
                        Price(approxSum.toString(), currencySymbol),
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
    }

    fun changeEditMode() {
        _uiState.update { state ->
            state.copy(
                editMode = when (state.editMode) {
                    EditMode.KEYBOARD -> EditMode.LIST
                    EditMode.LIST -> EditMode.KEYBOARD
                }
            )
        }
    }

    fun addNumber(btn: KeyPress) {
        _uiState.update { state ->
            val newPriceStr = MainPriceFieldParser.parse(state.price.value, btn)
            val newPrice = state.price.copy(value = newPriceStr)
            state.copy(price = newPrice)
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
