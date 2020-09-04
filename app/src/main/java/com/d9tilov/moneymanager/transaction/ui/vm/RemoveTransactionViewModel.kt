package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractor
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

class RemoveTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val fixedTransactionInteractor: FixedTransactionInteractor
) : BaseViewModel<RemoveTransactionDialogNavigator>() {

    fun removeTransaction(transaction: Transaction) {
        transactionInteractor.removeTransaction(transaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.remove() }
            .addTo(compositeDisposable)
    }

    fun removeFixedTransaction(fixedTransaction: FixedTransaction) {
        fixedTransactionInteractor.delete(fixedTransaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.remove() }
            .addTo(compositeDisposable)
    }
}
