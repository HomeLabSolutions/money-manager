package com.d9tilov.moneymanager.transaction.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.launch

class EditTransactionViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<EditTransactionNavigator>() {

    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionInteractor.update(transaction)
        navigator?.save()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(
                "create_transaction",
                "sum: " + transaction.sum + " category: " + transaction.category.name
            )
        }
    }
}
