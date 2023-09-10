package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.RegularExpenseNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularExpenseViewModel @Inject constructor(
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseRegularIncomeExpenseViewModel<RegularExpenseNavigator>() {

    val regularExpenseTransactionList: SharedFlow<List<RegularTransaction>> =
        regularTransactionInteractor.getAll(TransactionType.EXPENSE)
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    override fun onCheckClicked(regularTransaction: RegularTransaction) {
        viewModelScope.launch {
            regularTransactionInteractor.update(regularTransaction.copy(pushEnabled = !regularTransaction.pushEnabled))
        }
    }
}
