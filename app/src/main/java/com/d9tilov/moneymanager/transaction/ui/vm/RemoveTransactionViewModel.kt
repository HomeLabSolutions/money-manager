package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionInteractor
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

class RemoveTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val fixedTransactionInteractor: PeriodicTransactionInteractor
) : BaseViewModel<RemoveTransactionDialogNavigator>() {

    fun removeTransaction(transaction: Transaction) {
        transactionInteractor.removeTransaction(transaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.remove() }
            .addTo(compositeDisposable)
    }

    fun removeFixedTransaction(periodicTransaction: PeriodicTransaction) {
        fixedTransactionInteractor.delete(periodicTransaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.remove() }
            .addTo(compositeDisposable)
    }
}
