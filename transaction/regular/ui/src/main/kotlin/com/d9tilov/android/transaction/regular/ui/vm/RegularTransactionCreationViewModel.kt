package com.d9tilov.android.transaction.regular.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.MainPriceFieldParser
import com.d9tilov.android.core.utils.amountAfterCurrencyChange
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.moneyOrNull
import com.d9tilov.android.core.utils.toDecimal
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.ui.R
import com.d9tilov.android.transaction.regular.ui.navigator.RegularTransactionArgs
import com.d9tilov.android.transaction.regular.ui.vm.PeriodMenuItem.DAY
import com.d9tilov.android.transaction.regular.ui.vm.PeriodMenuItem.MONTH
import com.d9tilov.android.transaction.regular.ui.vm.PeriodMenuItem.WEEK
import com.d9tilov.android.transaction.regular.ui.vm.PeriodMenuItem.YEAR
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

data class RegularTransactionCreationUiState(
    val amount: String = "",
    val transaction: RegularTransaction = RegularTransaction.EMPTY,
) {
    companion object {
        val EMPTY = RegularTransactionCreationUiState()
    }
}

enum class PeriodMenuItem {
    DAY,
    WEEK,
    MONTH,
    YEAR,
}

enum class DaysInWeek(
    val id: Int,
) {
    MONDAY(R.string.regular_transaction_repeat_monday_short),
    TUESDAY(R.string.regular_transaction_repeat_tuesday_short),
    WEDNESDAY(R.string.regular_transaction_repeat_wednesday_short),
    THURSDAY(R.string.regular_transaction_repeat_thursday_short),
    FRIDAY(R.string.regular_transaction_repeat_friday_short),
    SATURDAY(R.string.regular_transaction_repeat_saturday_short),
    SUNDAY(R.string.regular_transaction_repeat_sunday_short),
}

fun ExecutionPeriod.toPeriodMenuItem(): PeriodMenuItem =
    when (this) {
        is ExecutionPeriod.EveryDay -> DAY
        is ExecutionPeriod.EveryWeek -> WEEK
        is ExecutionPeriod.EveryMonth -> MONTH
        is ExecutionPeriod.EveryYear -> YEAR
    }

@AssistedInject
class RegularTransactionCreationViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val regularTransactionInteractor: RegularTransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val currencyInteractor: CurrencyInteractor,
) : ViewModel() {
    private val regTransactionArgs: RegularTransactionArgs.RegularTransactionCreationArgs =
        RegularTransactionArgs.RegularTransactionCreationArgs(savedStateHandle)
    private val transactionId: Long = regTransactionArgs.transactionId
    private val transactionType: TransactionType = regTransactionArgs.transactionType
    private val _uiState = MutableStateFlow(RegularTransactionCreationUiState.EMPTY)
    val uiState: StateFlow<RegularTransactionCreationUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                var tr = state.transaction
                if (transactionId != NO_ID) {
                    tr = regularTransactionInteractor.getById(transactionId)
                    state.copy(
                        amount = tr.sum.stripTrailingZeros().toPlainString(),
                        transaction = tr.copy(type = transactionType),
                    )
                } else {
                    state.copy(
                        transaction =
                            tr.copy(
                                type = transactionType,
                                currencyCode =
                                    Currency.fromCode(currencyInteractor.getMainCurrency().code)?.name
                                        ?: Currency.USD.name,
                            ),
                    )
                }
            }
        }
    }

    fun updateAmount(amount: String) =
        _uiState.update { state ->
            val currency = Currency.fromCode(state.transaction.currencyCode)
            if (currency != null && MainPriceFieldParser.allowsAmountInput(amount, currency.precision)) {
                state.copy(amount = amount)
            } else {
                state
            }
        }

    fun updateCurrencyCode(code: String) {
        val currency = Currency.fromCode(code) ?: return
        _uiState.update { state ->
            if (state.transaction.currencyCode == code) {
                state
            } else {
                state.copy(
                    amount = state.amount.amountAfterCurrencyChange(currency),
                    transaction = state.transaction.copy(currencyCode = code),
                )
            }
        }
    }

    fun updateCategory(id: Long) =
        viewModelScope.launch {
            val category = categoryInteractor.getCategoryById(id)
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(category = category))
            }
        }

    fun updateCurPeriodItem(menuItem: PeriodMenuItem) {
        _uiState.update { state ->
            val currentPeriod = state.transaction.executionPeriod
            val executionPeriod =
                when (menuItem) {
                    DAY -> {
                        currentPeriod as? ExecutionPeriod.EveryDay
                            ?: ExecutionPeriod.EveryDay(currentDate().getStartOfDay())
                    }

                    WEEK -> {
                        currentPeriod as? ExecutionPeriod.EveryWeek
                            ?: ExecutionPeriod.EveryWeek(
                                dayOfWeek = currentDate().dayOfWeek.ordinal,
                                lastExecDate = currentDate().getStartOfDay(),
                            )
                    }

                    MONTH -> {
                        currentPeriod as? ExecutionPeriod.EveryMonth
                            ?: ExecutionPeriod.EveryMonth(
                                dayOfMonth = currentDate().day,
                                lastExecDate = currentDate().getStartOfDay(),
                            )
                    }

                    YEAR -> {
                        currentPeriod as? ExecutionPeriod.EveryYear
                            ?: ExecutionPeriod.EveryYear(
                                month = currentDate().month.number,
                                dayOfMonth = currentDate().day,
                                lastExecDate = currentDate().getStartOfDay(),
                            )
                    }
                }
            state.copy(transaction = state.transaction.copy(executionPeriod = executionPeriod))
        }
    }

    fun updateWeekDay(day: DaysInWeek) {
        _uiState.update { state ->
            val currentPeriod = state.transaction.executionPeriod
            if (currentPeriod is ExecutionPeriod.EveryWeek && currentPeriod.dayOfWeek == day.ordinal) {
                state
            } else {
                state.copy(
                    transaction =
                        state.transaction.copy(
                            executionPeriod =
                                ExecutionPeriod.EveryWeek(
                                    dayOfWeek = day.ordinal,
                                    lastExecDate = currentDate().getStartOfDay(),
                                ),
                        ),
                )
            }
        }
    }

    fun updateDayOfMonth(day: Int) {
        _uiState.update { state ->
            val currentPeriod = state.transaction.executionPeriod
            if (currentPeriod is ExecutionPeriod.EveryMonth && currentPeriod.dayOfMonth == day) {
                state
            } else {
                state.copy(
                    transaction =
                        state.transaction.copy(
                            executionPeriod =
                                ExecutionPeriod.EveryMonth(
                                    dayOfMonth = day,
                                    lastExecDate = currentDate().getStartOfDay(),
                                ),
                        ),
                )
            }
        }
    }

    fun updateDescription(description: String) {
        val tr = _uiState.value.transaction.copy(description = description)
        _uiState.update { state -> state.copy(transaction = tr) }
    }

    fun updateYearDate(date: LocalDate) {
        _uiState.update { state ->
            val currentPeriod = state.transaction.executionPeriod
            val month = date.month.number
            if (currentPeriod is ExecutionPeriod.EveryYear &&
                currentPeriod.month == month && currentPeriod.dayOfMonth == date.day
            ) {
                state
            } else {
                state.copy(
                    transaction =
                        state.transaction.copy(
                            executionPeriod =
                                ExecutionPeriod.EveryYear(
                                    month = month,
                                    dayOfMonth = date.day,
                                    lastExecDate = currentDate().getStartOfDay(),
                                ),
                        ),
                )
            }
        }
    }

    fun updatePushEnabled(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(transaction = state.transaction.copy(pushEnabled = enabled))
        }
    }

    suspend fun saveOrUpdate() {
        val state = _uiState.value
        val currency = Currency.fromCode(state.transaction.currencyCode) ?: return
        val amount = state.amount.moneyOrNull(currency)?.takeIf { it.value != 0L } ?: return
        val tr = state.transaction.copy(sum = amount.toDecimal())
        regularTransactionInteractor.insert(tr)
    }

    @AssistedFactory
    @ViewModelAssistedFactoryKey(RegularTransactionCreationViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): RegularTransactionCreationViewModel =
            create(extras.createSavedStateHandle())

        fun create(savedStateHandle: SavedStateHandle): RegularTransactionCreationViewModel
    }
}
