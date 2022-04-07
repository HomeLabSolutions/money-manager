package com.d9tilov.moneymanager.regular.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RegularTransactionCreatedNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
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
        .stateIn(viewModelScope, SharingStarted.Eagerly, RegularTransaction.EMPTY)

    fun saveOrUpdate(transaction: RegularTransaction) =
        viewModelScope.launch(Dispatchers.IO) {
            regularTransactionInteractor.insert(transaction)
            withContext(Dispatchers.Main) { navigator?.back() }
        }
}
