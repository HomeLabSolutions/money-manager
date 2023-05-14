package com.d9tilov.android.transaction.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.navigation.EditTransactionNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class EditTransactionViewModel @Inject constructor(private val transactionInteractor: TransactionInteractor)
    : BaseViewModel<EditTransactionNavigator>() {

    fun update(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionInteractor.update(transaction)
//            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
