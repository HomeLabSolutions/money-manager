package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.statistics.ui.navigation.StatisticsDetailsNavigator
import com.d9tilov.android.statistics.ui.navigation.TransactionDetailsArgs
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import com.d9tilov.android.transaction.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

data class StatisticsDetailsUiState(
    val categoryName: String = "",
    val transactions: List<TransactionUiModel> = emptyList(),
)

@HiltViewModel
class StatisticsDetailsViewModel
    @Inject constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        savedStateHandle: SavedStateHandle,
        transactionInteractor: TransactionInteractor,
        categoryInteractor: CategoryInteractor,
    ) : BaseViewModel<StatisticsDetailsNavigator>() {
        private val transactionDetailsArgs: TransactionDetailsArgs = TransactionDetailsArgs(savedStateHandle)
        private val _uiState = MutableStateFlow(StatisticsDetailsUiState())
        val uiState = _uiState.asStateFlow()

        init {
            viewModelScope.launch(ioDispatcher) {
                val transactionsDeferred =
                    async {
                        transactionInteractor.getTransactionsByCategoryId(
                            transactionDetailsArgs.categoryId,
                            transactionDetailsArgs.dateFrom.toLocalDateTime(),
                            transactionDetailsArgs.dateTo.toLocalDateTime(),
                            transactionDetailsArgs.inStatistics,
                        )
                    }

                val categoryDeferred = async { categoryInteractor.getCategoryById(transactionDetailsArgs.categoryId) }
                _uiState.update {
                    it.copy(
                        categoryName = categoryDeferred.await().name,
                        transactions = transactionsDeferred.await().map { it.toUiModel(showTime = true) },
                    )
                }
            }
        }
    }
