package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularIncomeNavigator
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularIncomeViewModel @Inject constructor(private val regularTransactionInteractor: RegularTransactionInteractor) :
    BaseRegularIncomeExpenseViewModel<RegularIncomeNavigator>() {

    val regularIncomeTransactionList: StateFlow<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.INCOME)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnabled = !regularTransaction.pushEnabled))
        }
    }
}
