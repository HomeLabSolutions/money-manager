package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegularTransactionCreationUiState(val transaction: RegularTransaction = RegularTransaction.EMPTY) {

    companion object {
        val EMPTY = RegularTransactionCreationUiState()
    }
}

@HiltViewModel
class RegularTransactionCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val regularTransactionInteractor: RegularTransactionInteractor,
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
                }
                state.copy(transaction = tr.copy(type = transactionType))
            }
        }
    }

    fun saveOrUpdate(transaction: RegularTransaction) = viewModelScope.launch {
        regularTransactionInteractor.insert(transaction)
    }
}
