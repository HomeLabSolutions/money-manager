package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor

class EditTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EditTransactionNavigator>()
