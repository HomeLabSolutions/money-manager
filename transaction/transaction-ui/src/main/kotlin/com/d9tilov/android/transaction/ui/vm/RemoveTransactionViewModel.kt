package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.RemoveTransactionDialogNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RemoveTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseViewModel<RemoveTransactionDialogNavigator>() {

    private val transactionId: Long = checkNotNull(savedStateHandle["transaction_id"])
    private val regularTransactionId: Long =
        checkNotNull(savedStateHandle["regular_transaction_id"])
    private val _transaction: StateFlow<Transaction> =
        transactionInteractor.getTransactionById(transactionId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Transaction.EMPTY
        )
    private val _regularTransaction: MutableStateFlow<RegularTransaction> =
        MutableStateFlow(RegularTransaction.EMPTY)

    init {
        viewModelScope.launch {
            _regularTransaction.value = regularTransactionInteractor.getById(regularTransactionId)
        }
    }

    fun remove() = viewModelScope.launch {
        launch { transactionInteractor.removeTransaction(_transaction.value) }
        launch { regularTransactionInteractor.delete(_regularTransaction.value) }
        withContext(Dispatchers.Main) { navigator?.remove() }
    }
}
