package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.EditTransactionNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<EditTransactionNavigator>() {

    val transactionType: TransactionType = checkNotNull(savedStateHandle["transaction_type"])
    private val editedTransactionId: Long = checkNotNull(savedStateHandle["edited_transaction_id"])
    val editedTransaction: StateFlow<Transaction> =
        transactionInteractor.getTransactionById(editedTransactionId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Transaction.EMPTY
        )

    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionInteractor.update(transaction)
        withContext(Dispatchers.Main) { navigator?.save() }
    }
}
