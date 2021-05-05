package com.d9tilov.moneymanager.periodic.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.PeriodicIncomeNavigator
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionInteractor
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import com.d9tilov.moneymanager.transaction.TransactionType

class PeriodicIncomeViewModel @ViewModelInject constructor(private val fixedTransactionInteractor: PeriodicTransactionInteractor) :
    BasePeriodicIncomeExpenseViewModel<PeriodicIncomeNavigator>() {

    init {
        fixedTransactionInteractor.getAll(TransactionType.INCOME)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { fixedIncomeTransactionList.value = it }
            .addTo(compositeDisposable)
    }

    override fun onCheckClicked(periodicTransaction: PeriodicTransaction) {
        fixedTransactionInteractor.update(periodicTransaction.copy(pushEnable = !periodicTransaction.pushEnable))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}
