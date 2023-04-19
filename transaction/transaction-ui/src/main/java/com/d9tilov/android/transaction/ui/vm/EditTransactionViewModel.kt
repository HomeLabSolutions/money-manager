package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.moneymanager.base.ui.navigator.EditTransactionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(private val transactionInteractor: TransactionInteractor)
    : ViewModel() {

    fun update(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionInteractor.update(transaction)
//            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
