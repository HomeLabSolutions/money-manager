package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.TransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionUiState(val transaction: Transaction = Transaction.EMPTY) {
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
                    _uiState.update { state: TransactionUiState -> state.copy(transaction = tr) }
                }
            }
        }
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionInteractor.update(transaction)
    }
}
