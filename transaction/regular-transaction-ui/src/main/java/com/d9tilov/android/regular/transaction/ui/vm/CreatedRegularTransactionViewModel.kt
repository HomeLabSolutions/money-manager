package com.d9tilov.android.regular.transaction.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionCreatedNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatedRegularTransactionViewModel @Inject constructor(
    private val regularTransactionInteractor: RegularTransactionInteractor
) : BaseViewModel<RegularTransactionCreatedNavigator>() {

    val defaultTransaction = regularTransactionInteractor.createDefault()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, com.d9tilov.android.regular.transaction.domain.model.RegularTransaction.EMPTY)

    fun saveOrUpdate(transaction: com.d9tilov.android.regular.transaction.domain.model.RegularTransaction) =
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.insert(transaction)
            withContext(Dispatchers.Main) { navigator?.back() }
        }
}
