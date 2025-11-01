package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuChartModel
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrencyType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatisticsType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.statistics.ui.model.chart.Pie
import com.d9tilov.android.statistics.ui.model.minus
import com.d9tilov.android.statistics.ui.model.plus
import com.d9tilov.android.statistics.ui.model.toTransactionType
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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
    val selectedPeriod: StatisticsPeriodModel = StatisticsPeriodModel.DAY(),
    val minMaxTransactionDate: TransactionMinMaxDateModel = TransactionMinMaxDateModel.EMPTY,
    val showNextArrow: Boolean = true,
    val showPrevArrow: Boolean = true,
    val periods: List<StatisticsPeriodModel> =
        listOf(
            StatisticsPeriodModel.DAY(),
            StatisticsPeriodModel.WEEK(),
            StatisticsPeriodModel.MONTH(),
            StatisticsPeriodModel.YEAR(),
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
    @Inject constructor(
        analyticsSender: AnalyticsSender,
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val transactionInteractor: TransactionInteractor,
        private val currencyInteractor: CurrencyInteractor,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(StatisticsUiState())
        val uiState = _uiState.asStateFlow()
        private val updateTrigger = MutableStateFlow(0)

        init {
            analyticsSender.send(
                AnalyticsEvent.Internal.Screen,
                mapOf(AnalyticsParams.Screen.Name to "statistics"),
            )
            viewModelScope.launch(ioDispatcher) {
                val currency = currencyInteractor.getMainCurrency()
                _uiState.update {
                    it.copy(
                        periodState =
                            it.periodState.copy(
                                minMaxTransactionDate =
                                    TransactionMinMaxDateModel(
                                        minDate = transactionInteractor.getTransactionMinMaxDate().minDate,
                                        maxDate = currentDateTime().getEndOfDay(),
                                    ),
                            ),
                    )
                }
                _uiState.update {
                    it.copy(
                        statisticsMenuState =
                            it.statisticsMenuState.copy(
                                currency =
                                    StatisticsMenuCurrencyType.Current(
                                        currency.code,
                                    ),
                            ),
                        periodState =
                            it.periodState.copy(
                                showNextArrow = showNextArrow(it.periodState.selectedPeriod),
                                showPrevArrow = showPrevArrow(it.periodState.selectedPeriod),
                            ),
                    )
                }
                updateTrigger
                    .flatMapLatest {
                        val (periodState: PeriodUiState, statisticsMenuState: StatisticsMenuState) = _uiState.value

                        val transactionsFlow =
                            transactionInteractor
                                .getTransactionsGroupedByCategory(
                                    statisticsMenuState.transactionType.toTransactionType(),
                                    periodState.selectedPeriod.from,
                                    periodState.selectedPeriod.to,
                                    statisticsMenuState.currency.currencyCode,
                                    statisticsMenuState.inStatistics == StatisticsMenuInStatisticsType.InStatisticsType,
                                    false,
                                ).map { list -> list.sortedByDescending { tr -> tr.sum } }

                        val sumFlow =
                            transactionInteractor.getSumInPeriod(
                                periodState.selectedPeriod.from,
                                periodState.selectedPeriod.to,
                                statisticsMenuState.transactionType.toTransactionType(),
                                statisticsMenuState.currency.currencyCode,
                                statisticsMenuState.inStatistics == StatisticsMenuInStatisticsType.InStatisticsType,
                            )

                        combine(transactionsFlow, sumFlow) { transactions, sum ->
                            transactions to sum
                        }
                    }.collect { (transactions, sum) ->
                        _uiState.update {
                            it.copy(
                                chartState =
                                    it.chartState.copy(
                                        pieData =
                                            transactions.map { item ->
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
                                        transactions = transactions,
                                        amount =
                                            it.detailsTransactionListState.amount.copy(
                                                sum.reduceScaleStr(),
                                                currency.symbol,
                                            ),
                                    ),
                            )
                        }
                    }
            }
        }

        fun updatePeriod(period: StatisticsPeriodModel) {
            _uiState.update {
                it.copy(
                    periodState =
                        it.periodState.copy(
                            selectedPeriod = period,
                            showNextArrow = showNextArrow(period),
                            showPrevArrow = showPrevArrow(period),
                        ),
                )
            }
            updateTrigger.update { it + 1 }
        }

        fun onMenuClick(type: StatisticsMenuType) =
            viewModelScope.launch {
                when (type) {
                    StatisticsMenuType.CURRENCY -> updateCurrency()
                    StatisticsMenuType.CHART -> {}
                    StatisticsMenuType.TRANSACTION_TYPE -> updateTransactionType()
                    StatisticsMenuType.STATISTICS -> updateInStatistics()
                }
                updateTrigger.update { it + 1 }
            }

        fun onPeriodArrowClicked(isForward: Boolean) {
            val period = _uiState.value.periodState.selectedPeriod
            val newPeriod =
                if (isForward) {
                    period plus 1
                } else {
                    period minus 1
                }
            _uiState.update {
                it.copy(
                    periodState =
                        it.periodState.copy(
                            selectedPeriod = newPeriod,
                            showNextArrow = showNextArrow(newPeriod),
                            showPrevArrow = showPrevArrow(newPeriod),
                        ),
                )
            }
            updateTrigger.update { it + 1 }
        }

        private fun showNextArrow(period: StatisticsPeriodModel): Boolean =
            period !is StatisticsPeriodModel.CUSTOM &&
                period
                    .to
                    .date
                    .plus(1, DateTimeUnit.DAY)
                    .getEndOfDay() <=
                _uiState
                    .value
                    .periodState
                    .minMaxTransactionDate
                    .maxDate

        private fun showPrevArrow(period: StatisticsPeriodModel): Boolean =
            period !is StatisticsPeriodModel.CUSTOM &&
                period
                    .from
                    .date
                    .minus(1, DateTimeUnit.DAY)
                    .getEndOfDay() >=
                _uiState
                    .value
                    .periodState
                    .minMaxTransactionDate
                    .minDate

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
