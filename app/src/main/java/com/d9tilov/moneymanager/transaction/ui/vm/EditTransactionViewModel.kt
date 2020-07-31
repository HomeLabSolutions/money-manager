package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import timber.log.Timber

class EditTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<EditTransactionNavigator>() {

    fun update(transaction: Transaction) {
        transactionInteractor.update(transaction)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                { navigator?.save() },
                { Timber.tag(App.TAG).d("Update transaction error: $it") }
            )
            .addTo(compositeDisposable)
    }
}
