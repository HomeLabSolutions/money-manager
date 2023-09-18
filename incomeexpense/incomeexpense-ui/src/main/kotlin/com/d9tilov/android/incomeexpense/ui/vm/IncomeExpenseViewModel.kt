package com.d9tilov.android.incomeexpense.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val expenseTransactions: List<Transaction> = emptyList(),
    val expenseInfo: Info? = null,
) {
    companion object {
        val EMPTY = ExpenseUiState()
    }
}

data class IncomeUiState(
    val incomeCategoryList: List<Category> = emptyList(),
    val incomeTransactions: List<Transaction> = emptyList(),
    val incomeInfo: Info? = null,
) {
    companion object {
        val EMPTY = IncomeUiState()
    }
}

data class Price(val value: String = "0", val currency: String = DEFAULT_CURRENCY_CODE) {
    companion object {
        val EMPTY = Price()
    }
}

data class Info(
    val wasSpendInPeriod: Price = Price.EMPTY,
    val canSpendToday: Price? = null,
    val wasSpendToday: Price? = null,
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

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    billingInteractor: BillingInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeExpenseUiState.EMPTY)
    val uiState = _uiState.asStateFlow()

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    init {
        viewModelScope.launch(updateCurrencyExceptionHandler) {
            currencyInteractor.getMainCurrencyFlow()
                .map { currency -> IncomeExpenseUiState(price = Price("0", currency.code)) }
                .collect { _uiState.value = it }
        }
    }
}
