package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuChartModel
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrencyType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatisticsType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.statistics.ui.model.chart.Pie
import com.d9tilov.android.statistics.ui.model.toTransactionType
import com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

data class StatisticsUiState(
    val periodState: PeriodUiState = PeriodUiState(),
    val statisticsMenuState: StatisticsMenuState = StatisticsMenuState(),
    val chartState: ChartState = ChartState(),
    val detailsTransactionListState: DetailsTransactionListState = DetailsTransactionListState(),
    val isPremium: Boolean = false,
)

data class PeriodUiState(
    val selectedPeriod: StatisticsPeriodModel = StatisticsPeriodModel.DAY,
    val periods: List<StatisticsPeriodModel> =
        listOf(
            StatisticsPeriodModel.DAY,
            StatisticsPeriodModel.WEEK,
            StatisticsPeriodModel.MONTH,
            StatisticsPeriodModel.YEAR,
            StatisticsPeriodModel.CUSTOM(),
        ),
)

data class StatisticsMenuState(
    val currency: StatisticsMenuCurrencyType = StatisticsMenuCurrencyType.Default,
    val chartType: StatisticsMenuChartModel = StatisticsMenuChartModel.PieChart,
    val transactionType: StatisticsMenuTransactionType = StatisticsMenuTransactionType.Expense,
    val inStatistics: StatisticsMenuInStatisticsType = StatisticsMenuInStatisticsType.InStatisticsType,
)

data class ChartState(
    val pieData: List<Pie> = emptyList(),
)

data class DetailsTransactionListState(
    val amount: DetailsSpentInPeriodState = DetailsSpentInPeriodState(),
    val transactions: List<TransactionChartModel> = emptyList(),
)

data class DetailsSpentInPeriodState(
    val value: String = "0",
    val currencySymbol: String = DEFAULT_CURRENCY_SYMBOL,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel
    @Inject
    constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val transactionInteractor: TransactionInteractor,
        private val currencyInteractor: CurrencyInteractor,
    ) : BaseViewModel<StatisticsNavigator>() {
        private val _uiState = MutableStateFlow(StatisticsUiState())
        val uiState = _uiState.asStateFlow()
        private val updateTrigger = MutableStateFlow(0)

        init {
            viewModelScope.launch(ioDispatcher) {
                val currency = currencyInteractor.getMainCurrency()
                launch {
                    _uiState.update {
                        it.copy(
                            statisticsMenuState =
                                it.statisticsMenuState.copy(
                                    currency =
                                        StatisticsMenuCurrencyType.Current(
                                            currency.code,
                                        ),
                                ),
                        )
                    }
                    updateTrigger
                        .flatMapLatest {
                            val (periodState: PeriodUiState, statisticsMenuState: StatisticsMenuState) = _uiState.value
                            transactionInteractor
                                .getTransactionsGroupedByCategory(
                                    statisticsMenuState.transactionType.toTransactionType(),
                                    periodState.selectedPeriod.from,
                                    periodState.selectedPeriod.to,
                                    statisticsMenuState.currency.currencyCode,
                                    statisticsMenuState.inStatistics ==
                                        StatisticsMenuInStatisticsType.InStatisticsType,
                                    false,
                                ).map { list -> list.sortedByDescending { tr -> tr.sum } }
                        }.collect { list: List<TransactionChartModel> ->
                            _uiState.update {
                                it.copy(
                                    chartState =
                                        it.chartState.copy(
                                            pieData =
                                                list.map { item ->
                                                    Pie(
                                                        label = item.category.name,
                                                        data = item.sum.toDouble(),
                                                        color = item.category.color,
                                                        selectedScale = 1.05f,
                                                    )
                                                },
                                        ),
                                    detailsTransactionListState =
                                        it.detailsTransactionListState.copy(
                                            transactions = list,
                                        ),
                                )
                            }
                        }
                }
                launch {
                    updateTrigger
                        .flatMapLatest {
                            val (periodState: PeriodUiState, statisticsMenuState: StatisticsMenuState) = _uiState.value
                            transactionInteractor
                                .getSumInPeriod(
                                    periodState.selectedPeriod.from,
                                    periodState.selectedPeriod.to,
                                    statisticsMenuState.transactionType.toTransactionType(),
                                    statisticsMenuState.currency.currencyCode,
                                    statisticsMenuState.inStatistics == StatisticsMenuInStatisticsType.InStatisticsType,
                                )
                        }.collect { sum ->
                            _uiState.update {
                                it.copy(
                                    detailsTransactionListState =
                                        it.detailsTransactionListState.copy(
                                            amount =
                                                DetailsSpentInPeriodState(
                                                    sum.reduceScaleStr(),
                                                    currency.symbol,
                                                ),
                                        ),
                                )
                            }
                        }
                }
            }
        }

        fun updatePeriod(period: StatisticsPeriodModel) {
            _uiState.update { it.copy(periodState = it.periodState.copy(selectedPeriod = period)) }
            updateTrigger.update { it + 1 }
        }

        fun onMenuClick(type: StatisticsMenuType) =
            viewModelScope.launch {
                when (type) {
                    StatisticsMenuType.CURRENCY -> updateCurrency()
                    StatisticsMenuType.CHART -> TODO()
                    StatisticsMenuType.TRANSACTION_TYPE -> updateTransactionType()
                    StatisticsMenuType.STATISTICS -> updateInStatistics()
                }
                updateTrigger.update { it + 1 }
            }

        private suspend fun updateCurrency() {
            val currency = currencyInteractor.getMainCurrency()
            val curCurrency = _uiState.value.statisticsMenuState.currency
            val newCurrency: StatisticsMenuCurrencyType =
                when (curCurrency) {
                    is StatisticsMenuCurrencyType.Current -> StatisticsMenuCurrencyType.Default
                    is StatisticsMenuCurrencyType.Default -> StatisticsMenuCurrencyType.Current(currency.code)
                }
            _uiState.update { it.copy(statisticsMenuState = it.statisticsMenuState.copy(currency = newCurrency)) }
        }

        private fun updateTransactionType() {
            val currentTransactionType = _uiState.value.statisticsMenuState.transactionType
            val newTransactionType =
                when (currentTransactionType) {
                    StatisticsMenuTransactionType.Expense -> StatisticsMenuTransactionType.Income
                    StatisticsMenuTransactionType.Income -> StatisticsMenuTransactionType.Expense
                }
            _uiState.update {
                it.copy(statisticsMenuState = it.statisticsMenuState.copy(transactionType = newTransactionType))
            }
        }

        private fun updateInStatistics() {
            val currentInStatistics = _uiState.value.statisticsMenuState.inStatistics
            val newInStatistics =
                when (currentInStatistics) {
                    StatisticsMenuInStatisticsType.InStatisticsType -> StatisticsMenuInStatisticsType.All
                    StatisticsMenuInStatisticsType.All -> StatisticsMenuInStatisticsType.InStatisticsType
                }
            _uiState.update {
                it.copy(statisticsMenuState = it.statisticsMenuState.copy(inStatistics = newInStatistics))
            }
        }
    }
