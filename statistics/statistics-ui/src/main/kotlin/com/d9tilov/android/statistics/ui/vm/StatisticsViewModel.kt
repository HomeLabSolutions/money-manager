package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.di.CoroutinesModule.Companion.DISPATCHER_IO
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCategoryType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuChartModel
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrencyType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatisticsType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.statistics.ui.model.toTransactionType
import com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

data class StatisticsUiState(
    val periodState: PeriodUiState = PeriodUiState(),
    val statisticsMenuState: StatisticsMenuState = StatisticsMenuState(),
    val detailsTransactionListState: DetailsTransactionListState = DetailsTransactionListState(),
    val isPremium: Boolean = false,
)

data class PeriodUiState(
    val selectedPeriod: StatisticsPeriodModel = StatisticsPeriodModel.MONTH,
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
    val menuMap: MutableMap<StatisticsMenuType, BaseStatisticsMenuType> =
        mutableMapOf(
            StatisticsMenuType.CURRENCY to StatisticsMenuCurrencyType.Default,
            StatisticsMenuType.CHART to StatisticsMenuChartModel.PieChart,
            StatisticsMenuType.CATEGORY_TYPE to StatisticsMenuCategoryType.Child,
            StatisticsMenuType.TRANSACTION_TYPE to StatisticsMenuTransactionType.Expense,
            StatisticsMenuType.STATISTICS to StatisticsMenuInStatisticsType.InStatisticsType,
        ),
)

data class DetailsTransactionListState(
    val amount: DetailsSpentInPeriodState = DetailsSpentInPeriodState(),
    val transactions: List<TransactionChartModel> = emptyList(),
)

data class DetailsSpentInPeriodState(
    val value: String = "0",
    val currencySymbol: String = DEFAULT_CURRENCY_SYMBOL,
)

@HiltViewModel
class StatisticsViewModel
    @Inject
    constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val transactionInteractor: TransactionInteractor,
        currencyInteractor: CurrencyInteractor,
    ) : BaseViewModel<StatisticsNavigator>() {
        private val _uiState = MutableStateFlow(StatisticsUiState())
        val uiState = _uiState.asStateFlow()

        init {
            System.out.println("moggot aaa")
            viewModelScope.launch {
                val currency = currencyInteractor.getMainCurrency()
                _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.CURRENCY] =
                    StatisticsMenuCurrencyType.Current(currency.code)
                val transactionType =
                    _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.TRANSACTION_TYPE]
                        as StatisticsMenuTransactionType
                val chartPeriod = _uiState.value.periodState.selectedPeriod
                val currencyType =
                    _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.CURRENCY]
                        as StatisticsMenuCurrencyType
                val inStatistics =
                    _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.STATISTICS]
                        as StatisticsMenuInStatisticsType
                val categoryType =
                    _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.CATEGORY_TYPE]
                        as StatisticsMenuCategoryType
                launch {

                    transactionInteractor
                        .getTransactionsGroupedByCategory(
                            transactionType.toTransactionType(),
                            chartPeriod.from,
                            chartPeriod.to,
                            currencyType.currencyCode,
                            inStatistics == StatisticsMenuInStatisticsType.InStatisticsType,
                            categoryType == StatisticsMenuCategoryType.Child,
                        ).map { list -> list.sortedByDescending { tr -> tr.sum } }
                        .flowOn(ioDispatcher)
                        .catch { }
                        .collect { list: List<TransactionChartModel> ->
                            System.out.println("moggot updateList: $list")
                            _uiState.update {
                                it.copy(
                                    detailsTransactionListState =
                                        it.detailsTransactionListState.copy(
                                            transactions = list,
                                        ),
                                )
                            }
                        }
                }
                System.out.println("moggot from: ${_uiState.value.periodState.selectedPeriod.from}")
                System.out.println("moggot to: ${_uiState.value.periodState.selectedPeriod.to}")
                launch {
                    transactionInteractor
                        .getSumSpentInPeriod(
                            _uiState.value.periodState.selectedPeriod.from,
                            _uiState.value.periodState.selectedPeriod.to,
                        ).collect { sum ->
                            System.out.println("moggot spentInPeriod: sum: $sum")
                            _uiState.update {
                                it.copy(
                                    detailsTransactionListState =
                                        it.detailsTransactionListState.copy(
                                            amount =
                                                DetailsSpentInPeriodState(
                                                    sum.toString(),
                                                    currency.symbol,
                                                ),
                                        ),
                                )
                            }
                        }
                }
            }
        }
    }
