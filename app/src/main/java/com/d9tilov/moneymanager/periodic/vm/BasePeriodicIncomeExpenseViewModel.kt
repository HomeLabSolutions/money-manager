package com.d9tilov.moneymanager.periodic.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.BasePeriodicIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction

abstract class BasePeriodicIncomeExpenseViewModel<T : BasePeriodicIncomeExpenseNavigator> :
    BaseViewModel<T>() {

    protected var fixedIncomeTransactionList = MutableLiveData<List<PeriodicTransaction>>()
    protected var fixedExpenseTransactionList = MutableLiveData<List<PeriodicTransaction>>()

    abstract fun onCheckClicked(periodicTransaction: PeriodicTransaction)

    fun getFixedIncomeTransactionList(): LiveData<List<PeriodicTransaction>> =
        fixedIncomeTransactionList

    fun getFixedExpenseTransactionList(): LiveData<List<PeriodicTransaction>> =
        fixedExpenseTransactionList
}
