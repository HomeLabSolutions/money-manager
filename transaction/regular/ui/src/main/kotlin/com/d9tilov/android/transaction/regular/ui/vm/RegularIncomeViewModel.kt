package com.d9tilov.android.transaction.regular.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.ui.navigator.RegularIncomeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularIncomeViewModel
    @Inject
    constructor(
        private val regularTransactionInteractor: RegularTransactionInteractor,
    ) : BaseRegularIncomeExpenseViewModel<RegularIncomeNavigator>() {
        val regularIncomeTransactionList: SharedFlow<List<RegularTransaction>> =
            regularTransactionInteractor
                .getAll(TransactionType.INCOME)
                .distinctUntilChanged()
                .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

        override fun onCheckClicked(regularTransaction: RegularTransaction) {
            viewModelScope.launch {
                regularTransactionInteractor.update(
                    regularTransaction.copy(pushEnabled = !regularTransaction.pushEnabled),
                )
            }
        }
    }
