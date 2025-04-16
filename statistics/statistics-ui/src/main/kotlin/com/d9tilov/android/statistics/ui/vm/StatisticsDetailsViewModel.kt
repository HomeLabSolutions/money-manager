package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator
import com.d9tilov.android.statistics.ui.navigation.TransactionDetailsArgs
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatisticsDetailsUiState(
    val transactions: List<Transaction> = emptyList(),
)

@HiltViewModel
class StatisticsDetailsViewModel
    @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val transactionInteractor: TransactionInteractor,
        private val categoryInteractor: CategoryInteractor,
    ) : BaseViewModel<StatisticsDetailsNavigator>() {
        private val transactionDetailsArgs: TransactionDetailsArgs = TransactionDetailsArgs(savedStateHandle)
        private val _uiState = MutableStateFlow(StatisticsDetailsUiState())
        val uiState = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                System.out.println("moggot args: $transactionDetailsArgs")
            }
        }
    }
