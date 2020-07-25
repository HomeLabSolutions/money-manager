package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialog

class RemoveTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<RemoveTransactionNavigator>() {

    fun remove() {
        val transactionToDelete =
            savedStateHandle.get<Transaction>(TransactionRemoveDialog.ARG_REMOVE_TRANSACTION)
                ?: throw
                IllegalArgumentException("Transaction to delete is null")
        transactionInteractor.removeTransaction(transactionToDelete)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.remove() }
            .addTo(compositeDisposable)
    }

    fun cancel() {
        navigator?.cancel()
    }
}
