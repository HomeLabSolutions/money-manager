package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularIncomeNavigator
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularIncomeViewModel @Inject constructor(private val regularTransactionInteractor: RegularTransactionInteractor) :
    BaseRegularIncomeExpenseViewModel<RegularIncomeNavigator>() {

    val regularIncomeTransactionList: LiveData<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.INCOME).asLiveData()

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnable = !regularTransaction.pushEnable))
        }
    }
}
