package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.common.android.utils.TRANSACTION_DATE_FORMAT
import com.d9tilov.android.common.android.utils.formatDate
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.ui.navigation.TransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionUiState(
    val amount: String = "0",
    val currency: String = DEFAULT_CURRENCY_SYMBOL,
    val inStatistics: Boolean = true,
    val description: String = "",
    val category: Category = Category.EMPTY_EXPENSE,
    val date: String = formatDate(currentDateTime(), TRANSACTION_DATE_FORMAT),
) {
    companion object {
        val EMPTY = TransactionUiState()
    }
}

@HiltViewModel
class TransactionCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState.EMPTY)
    val uiState = _uiState.asStateFlow()
    private val categoryArgs: TransactionArgs.TransactionCreationArgs =
        TransactionArgs.TransactionCreationArgs(savedStateHandle)
    private val transactionId: Long = categoryArgs.transactionId

    init {
        val transactionExceptionHandler = CoroutineExceptionHandler { _, _ -> }
        viewModelScope.launch(transactionExceptionHandler) {
            launch {
                transactionInteractor.getTransactionById(transactionId).collect { tr ->
                    _uiState.update { state: TransactionUiState -> state.copy(
                        amount = tr.sum.toString(),
                        currency = tr.currencyCode.getSymbolByCode(),
                        description = tr.description,
                        inStatistics = tr.inStatistics,
                        category = tr.category,
                        date = formatDate(tr.date, TRANSACTION_DATE_FORMAT),
                    ) }
                }
            }
        }
    }

    fun updateAmount(amount: String)= _uiState.update { state -> state.copy(amount = amount) }
}
