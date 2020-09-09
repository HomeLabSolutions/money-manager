package com.d9tilov.moneymanager.fixed.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.BaseFixedIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction

abstract class BaseFixedIncomeExpenseViewModel<T : BaseFixedIncomeExpenseNavigator> :
    BaseViewModel<T>() {

    var fixedIncomeTransactionList = MutableLiveData<List<FixedTransaction>>()
    var fixedExpenseTransactionList = MutableLiveData<List<FixedTransaction>>()

    abstract fun onCheckClicked(fixedTransaction: FixedTransaction)
}
