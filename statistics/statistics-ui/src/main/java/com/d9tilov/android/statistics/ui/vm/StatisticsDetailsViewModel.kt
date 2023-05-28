package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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