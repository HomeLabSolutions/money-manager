package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.R
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.navigation.TransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionInfoUiState(
    val mode: TransactionInfoMode = TransactionInfoMode.VIEW,
    val amount: String = "0",
    val transaction: Transaction =
        Transaction.EMPTY.copy(
            category =
                Category.EMPTY_EXPENSE.copy(
                    color = android.R.color.transparent,
                    icon = R.drawable.dummy_icon,
                ),
        ),
) {
    companion object {
        val EMPTY = TransactionInfoUiState()
    }
}

@HiltViewModel
class TransactionInfoViewModel
    @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val transactionInteractor: TransactionInteractor,
        private val categoryInteractor: CategoryInteractor,
    ) : ViewModel() {
        private val transactionArgs: TransactionArgs.TransactionInfoArgs =
            TransactionArgs.TransactionInfoArgs(savedStateHandle)
        private val _uiState = MutableStateFlow(TransactionInfoUiState.EMPTY)
        val uiState = _uiState.asStateFlow()
        private val transactionId: Long = transactionArgs.transactionId

        init {
            val transactionExceptionHandler = CoroutineExceptionHandler { _, _ -> }
            viewModelScope.launch(transactionExceptionHandler) {
                launch {
                    transactionInteractor.getTransactionById(transactionId).collect { tr ->
                        _uiState.update { state: TransactionInfoUiState ->
                            state.copy(mode = transactionArgs.mode, amount = tr.sum.reduceScaleStr(), transaction = tr)
                        }
                    }
                }
            }
        }

        fun updateAmount(amount: String) = _uiState.update { state -> state.copy(amount = amount) }

        fun updateDescription(description: String) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(description = description))
            }

        fun updateDate(date: Long) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(date = date.toLocalDateTime()))
            }

        fun updateInStatistics(inStatistics: Boolean) =
            _uiState.update { state ->
                val tr = state.transaction
                state.copy(transaction = tr.copy(inStatistics = inStatistics))
            }

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

        suspend fun save() {
            val state = _uiState.value
            if (state.mode == TransactionInfoMode.VIEW) return
            transactionInteractor.update(
                state.transaction.copy(sum = state.amount.toBigDecimal()),
            )
        }
    }
