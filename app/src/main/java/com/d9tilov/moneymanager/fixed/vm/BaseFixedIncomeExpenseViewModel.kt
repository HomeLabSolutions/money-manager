package com.d9tilov.moneymanager.fixed.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.BaseFixedIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction

abstract class BaseFixedIncomeExpenseViewModel<T : BaseFixedIncomeExpenseNavigator> :
    BaseViewModel<T>() {

    protected var fixedIncomeTransactionList = MutableLiveData<List<FixedTransaction>>()
    protected var fixedExpenseTransactionList = MutableLiveData<List<FixedTransaction>>()

    abstract fun onCheckClicked(fixedTransaction: FixedTransaction)

    fun getFixedIncomeTransactionList(): LiveData<List<FixedTransaction>> =
        fixedIncomeTransactionList

    fun getFixedExpenseTransactionList(): LiveData<List<FixedTransaction>> =
        fixedExpenseTransactionList
}
