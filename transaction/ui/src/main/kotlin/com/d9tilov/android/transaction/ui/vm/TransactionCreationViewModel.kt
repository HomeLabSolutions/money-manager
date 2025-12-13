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
        val EMPTY = TransactionUiState()
    }
}

@HiltViewModel
class TransactionCreationViewModel
    @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val transactionInteractor: TransactionInteractor,
        private val categoryInteractor: CategoryInteractor,
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
                        _uiState.update { state: TransactionUiState ->
                            state.copy(amount = tr.sum.reduceScaleStr(), transaction = tr)
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

        fun save() =
            viewModelScope.launch {
                transactionInteractor.update(
                    _uiState.value.transaction.copy(sum = _uiState.value.amount.toBigDecimal()),
                )
            }
    }
