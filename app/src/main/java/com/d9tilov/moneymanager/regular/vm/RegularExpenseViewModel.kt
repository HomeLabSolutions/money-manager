package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularExpenseNavigator
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularExpenseViewModel @Inject constructor(
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseRegularIncomeExpenseViewModel<RegularExpenseNavigator>() {

    val regularExpenseTransactionList: LiveData<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.EXPENSE).asLiveData()

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnabled = !regularTransaction.pushEnabled))
        }
    }
}
