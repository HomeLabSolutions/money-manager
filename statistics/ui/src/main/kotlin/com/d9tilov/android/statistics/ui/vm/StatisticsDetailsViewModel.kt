package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.statistics.ui.navigation.TransactionDetailsArgs
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import com.d9tilov.android.transaction.ui.model.toUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Named
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StatisticsDetailsUiState(
    val categoryName: String = "",
    val transactions: List<TransactionUiModel> = emptyList(),
)

@AssistedInject
class StatisticsDetailsViewModel(
    analyticsSender: AnalyticsSender,
    @Assisted savedStateHandle: SavedStateHandle,
    transactionInteractor: TransactionInteractor,
    categoryInteractor: CategoryInteractor,
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val transactionDetailsArgs: TransactionDetailsArgs = TransactionDetailsArgs(savedStateHandle)
    private val _uiState = MutableStateFlow(StatisticsDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        analyticsSender.send(
            AnalyticsEvent.Internal.Screen,
            mapOf(AnalyticsParams.Screen.Name to "statistics_details"),
        )
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
                    transactions =
                        transactionsDeferred
                            .await()
                            .sortedByDescending { transaction -> transaction.date }
                            .map { transaction -> transaction.toUiModel(isStatisticsScreen = true) },
                )
            }
        }
    }

    @AssistedFactory
    @ViewModelAssistedFactoryKey(StatisticsDetailsViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): StatisticsDetailsViewModel =
            create(extras.createSavedStateHandle())

        fun create(savedStateHandle: SavedStateHandle): StatisticsDetailsViewModel
    }
}
