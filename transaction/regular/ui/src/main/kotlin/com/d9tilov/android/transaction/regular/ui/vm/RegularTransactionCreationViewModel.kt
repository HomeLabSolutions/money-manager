package com.d9tilov.android.transaction.regular.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.ui.R
import com.d9tilov.android.transaction.regular.ui.navigator.RegularTransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegularTransactionCreationUiState(
    val amount: String = "",
    val curPeriodItem: PeriodMenuItem = PeriodMenuItem.MONTH,
    val curDayInWeek: DaysInWeek = DaysInWeek.MONDAY,
    val curDayOfMonth: Int = 1,
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

@HiltViewModel
class RegularTransactionCreationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
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
                            amount = tr.sum.toString(),
                            transaction = tr.copy(type = transactionType),
                        )
                    } else {
                        state.copy(
                            transaction =
                                tr.copy(
                                    type = transactionType,
                                    currencyCode = currencyInteractor.getMainCurrency().code,
                                ),
                        )
                    }
                }
            }
        }

        fun updateAmount(amount: String) = _uiState.update { state -> state.copy(amount = amount) }

        fun updateCurrencyCode(code: String) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(currencyCode = code))
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
            _uiState.update { state -> state.copy(curPeriodItem = menuItem) }
        }

        fun updateWeekDay(day: DaysInWeek) {
            _uiState.update { state -> state.copy(curDayInWeek = day) }
        }

        fun updateDayOfMonth(day: Int) {
            _uiState.update { state -> state.copy(curDayOfMonth = day) }
        }

        fun updateDescription(description: String) {
            val tr = _uiState.value.transaction.copy(description = description)
            _uiState.update { state -> state.copy(transaction = tr) }
        }

        fun saveOrUpdate() =
            viewModelScope.launch {
                val tr =
                    when (_uiState.value.curPeriodItem) {
                        PeriodMenuItem.DAY ->
                            _uiState.value.transaction.copy(
                                executionPeriod = ExecutionPeriod.EveryDay(),
                            )
                        PeriodMenuItem.WEEK ->
                            _uiState.value.transaction.copy(
                                executionPeriod =
                                    ExecutionPeriod.EveryWeek(
                                        _uiState.value.curDayInWeek.ordinal,
                                    ),
                            )

                        PeriodMenuItem.MONTH ->
                            _uiState.value.transaction.copy(
                                executionPeriod =
                                    ExecutionPeriod.EveryMonth(
                                        _uiState.value.curDayOfMonth,
                                    ),
                            )
                    }
                regularTransactionInteractor.insert(tr.copy(sum = _uiState.value.amount.toBigDecimal()))
            }
    }
