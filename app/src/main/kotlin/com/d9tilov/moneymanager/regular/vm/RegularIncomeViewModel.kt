package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularIncomeNavigator
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularIncomeViewModel @Inject constructor(private val regularTransactionInteractor: RegularTransactionInteractor) :
    BaseRegularIncomeExpenseViewModel<RegularIncomeNavigator>() {

    val regularIncomeTransactionList: SharedFlow<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.INCOME)
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnabled = !regularTransaction.pushEnabled))
        }
    }
}
