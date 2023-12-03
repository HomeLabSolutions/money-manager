package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegularTransactionListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val regularTransactionInteractor: RegularTransactionInteractor,
) : ViewModel() {

    private val regularTransactionArgs: RegularTransactionArgs.RegularTransactionListArgs =
        RegularTransactionArgs.RegularTransactionListArgs(savedStateHandle)
    val transactionType = checkNotNull(regularTransactionArgs.transactionType)
}
