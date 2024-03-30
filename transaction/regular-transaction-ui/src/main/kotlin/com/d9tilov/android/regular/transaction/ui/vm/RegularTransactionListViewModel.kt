package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegularTransactionListState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val regularTransactions: List<RegularTransaction> = emptyList(),
) {
    companion object {
        val EMPTY = RegularTransactionListState()
    }
}

@HiltViewModel
class RegularTransactionListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val regularTransactionInteractor: RegularTransactionInteractor,
) : ViewModel() {

    private val regularTransactionArgs: RegularTransactionArgs.RegularTransactionListArgs =
        RegularTransactionArgs.RegularTransactionListArgs(savedStateHandle)
    private val transactionType = checkNotNull(regularTransactionArgs.transactionType)

    private val _uiState = MutableStateFlow(RegularTransactionListState.EMPTY)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            regularTransactionInteractor.getAll(transactionType)
                .collect { list ->
                    _uiState.update { state ->
                        state.copy(
                            transactionType = transactionType,
                            regularTransactions = list
                        )
                    }
                }
        }
    }

    fun removeTransaction(transaction: RegularTransaction) = viewModelScope.launch {
        regularTransactionInteractor.delete(transaction)
    }
}
