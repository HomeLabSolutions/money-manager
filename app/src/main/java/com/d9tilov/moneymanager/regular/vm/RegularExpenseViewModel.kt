package com.d9tilov.moneymanager.regular.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularExpenseNavigator
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.launch

class RegularExpenseViewModel @ViewModelInject constructor(private val regularTransactionInteractor: RegularTransactionInteractor) :
    BaseRegularIncomeExpenseViewModel<RegularExpenseNavigator>() {

    lateinit var regularExpenseTransactionList: LiveData<List<RegularTransaction>>

    init {
        viewModelScope.launch {
            regularExpenseTransactionList =
                regularTransactionInteractor.getAll(TransactionType.EXPENSE).asLiveData()
        }
    }

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnable = !regularTransaction.pushEnable))
        }
    }
}
