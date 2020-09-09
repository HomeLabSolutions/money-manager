package com.d9tilov.moneymanager.fixed.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractor
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.transaction.TransactionType

class FixedExpenseViewModel @ViewModelInject constructor(private val fixedTransactionInteractor: FixedTransactionInteractor) :
    BaseViewModel<FixedExpenseNavigator>() {

    var fixedExpenseList = MutableLiveData<List<FixedTransaction>>()

    init {
        fixedTransactionInteractor.getAll(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { fixedExpenseList.value = it }
            .addTo(compositeDisposable)
    }

    fun onCheckClicked(fixedTransaction: FixedTransaction) {
        fixedTransactionInteractor.update(fixedTransaction.copy(pushEnable = !fixedTransaction.pushEnable))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}
