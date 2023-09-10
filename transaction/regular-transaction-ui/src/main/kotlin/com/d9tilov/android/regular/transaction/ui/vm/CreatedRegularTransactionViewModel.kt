package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionCreatedNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatedRegularTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseViewModel<RegularTransactionCreatedNavigator>() {

    val transactionType: TransactionType = checkNotNull(savedStateHandle["transaction_type"])
    private val regularTransactionId: Long = checkNotNull(savedStateHandle["regular_transaction_id"])
    private val _regularTransaction: MutableStateFlow<RegularTransaction> = MutableStateFlow(RegularTransaction.EMPTY)
    val regularTransaction: StateFlow<RegularTransaction> = _regularTransaction

    init {
        viewModelScope.launch {
            _regularTransaction.value = regularTransactionInteractor.getById(regularTransactionId).copy(type = transactionType)
        }
    }

    fun saveOrUpdate(transaction: RegularTransaction) = viewModelScope.launch {
        regularTransactionInteractor.insert(transaction)
        withContext(Dispatchers.Main) { navigator?.back() }
    }
}
