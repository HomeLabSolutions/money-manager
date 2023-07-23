package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.RemoveTransactionDialogNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RemoveTransactionViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseViewModel<RemoveTransactionDialogNavigator>() {

    fun removeTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionInteractor.removeTransaction(transaction)
            withContext(Dispatchers.Main) { navigator?.remove() }
        }
    }

    fun removeRegularTransaction(regularTransaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.delete(regularTransaction)
            withContext(Dispatchers.Main) { navigator?.remove() }
        }
    }
}
