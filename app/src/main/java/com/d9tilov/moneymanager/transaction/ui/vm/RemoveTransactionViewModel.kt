package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import kotlinx.coroutines.launch

class RemoveTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseViewModel<RemoveTransactionDialogNavigator>() {

    fun removeTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionInteractor.removeTransaction(transaction)
        navigator?.remove()
    }

    fun removeRegularTransaction(regularTransaction: RegularTransaction) = viewModelScope.launch {
        regularTransactionInteractor.delete(regularTransaction)
        navigator?.remove()
    }
}
