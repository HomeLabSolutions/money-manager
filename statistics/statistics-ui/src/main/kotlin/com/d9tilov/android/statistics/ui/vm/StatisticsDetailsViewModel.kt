package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionInteractor: TransactionInteractor,
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<StatisticsDetailsNavigator>() {

    private val categoryId: Long = checkNotNull(savedStateHandle["category_id"])
    private val transactionType: TransactionType =
        checkNotNull(savedStateHandle["transaction_type"])
    private val startPeriod: Long = checkNotNull(savedStateHandle["start_period"])
    private val endPeriod: Long = checkNotNull(savedStateHandle["end_period"])
    private val inStatistics: Boolean = checkNotNull(savedStateHandle["in_statistics"])
    private val _category = MutableStateFlow(Category.EMPTY_INCOME)
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val category: StateFlow<Category> = _category
    val transactions: StateFlow<List<Transaction>> = _transactions

    init {
        viewModelScope.launch {
            _category.value = categoryInteractor.getCategoryById(categoryId)
            _transactions.value = transactionInteractor.getTransactionsByCategory(
                transactionType,
                _category.value,
                startPeriod.toLocalDateTime().getStartOfDay(),
                endPeriod.toLocalDateTime().getEndOfDay(),
                inStatistics
            ).sortedByDescending { item -> item.date }
        }
    }
}
