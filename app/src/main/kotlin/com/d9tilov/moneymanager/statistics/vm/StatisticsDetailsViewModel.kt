package com.d9tilov.moneymanager.statistics.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsDetailsNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.core.util.toLocalDateTime
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<StatisticsDetailsNavigator>() {

    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            transactions.value = transactionInteractor.getTransactionsByCategory(
                savedStateHandle.get<TransactionType>("transactionType")
                    ?: throw IllegalArgumentException("TransactionType is null"),
                savedStateHandle.get<Category>("category")
                    ?: throw IllegalArgumentException("Category is null"),
                savedStateHandle.get<Long>("start_period")?.toLocalDateTime()?.getStartOfDay()
                    ?: throw IllegalArgumentException("Start period is null"),
                savedStateHandle.get<Long>("end_period")?.toLocalDateTime()?.getEndOfDay()
                    ?: throw IllegalArgumentException("End period is null"),
                savedStateHandle.get<Boolean>("in_statistics") ?: true
            ).sortedByDescending { item -> item.date }
        }
    }

    fun getTransactions(): StateFlow<List<Transaction>> = transactions
}
