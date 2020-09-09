package com.d9tilov.moneymanager.fixed.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractor
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.transaction.TransactionType

class FixedExpenseViewModel @ViewModelInject constructor(private val fixedTransactionInteractor: FixedTransactionInteractor) :
    BaseFixedIncomeExpenseViewModel<FixedExpenseNavigator>() {

    init {
        fixedTransactionInteractor.getAll(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { fixedExpenseTransactionList.value = it }
            .addTo(compositeDisposable)
    }

    override fun onCheckClicked(fixedTransaction: FixedTransaction) {
        fixedTransactionInteractor.update(fixedTransaction.copy(pushEnable = !fixedTransaction.pushEnable))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}
