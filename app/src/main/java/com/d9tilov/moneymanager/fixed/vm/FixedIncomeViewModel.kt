package com.d9tilov.moneymanager.fixed.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.FixedIncomeNavigator
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractor
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.transaction.TransactionType

class FixedIncomeViewModel @ViewModelInject constructor(private val fixedTransactionInteractor: FixedTransactionInteractor) :
    BaseFixedIncomeExpenseViewModel<FixedIncomeNavigator>() {

    init {
        fixedTransactionInteractor.getAll(TransactionType.INCOME)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { fixedIncomeTransactionList.value = it }
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
