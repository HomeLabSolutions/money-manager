package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common.android.di.CoroutinesModule.Companion.DISPATCHER_IO
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCategoryType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrency
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatistics
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuChartModel
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionLineChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

data class StatisticsUiState(
    val periodState: PeriodUiState = PeriodUiState(),
    val statisticsMenuState: StatisticsMenuState = StatisticsMenuState(),
    val detailsTransactionListState: DetailsTransactionListState = DetailsTransactionListState(),
    val isPremium: Boolean = false
)

data class PeriodUiState(
    val selectedPeriod: StatisticsPeriodModel = StatisticsPeriodModel.MONTH,
    val periods: List<StatisticsPeriodModel> = listOf(
        StatisticsPeriodModel.DAY,
        StatisticsPeriodModel.WEEK,
        StatisticsPeriodModel.MONTH,
        StatisticsPeriodModel.YEAR,
        StatisticsPeriodModel.CUSTOM()
    )
)

data class StatisticsMenuState(
    val menuMap: MutableMap<StatisticsMenuType, BaseStatisticsMenuType> = mutableMapOf(
        StatisticsMenuType.CURRENCY to StatisticsMenuCurrency.Default,
        StatisticsMenuType.CHART to StatisticsMenuChartModel.PieChart,
        StatisticsMenuType.CATEGORY_TYPE to StatisticsMenuCategoryType.Child,
        StatisticsMenuType.TRANSACTION_TYPE to StatisticsMenuTransactionType.Expense,
        StatisticsMenuType.STATISTICS to StatisticsMenuInStatistics.InStatistics
    )
)

data class DetailsTransactionListState(
    val spentInPeriod: String = "$0",
    val transactions: List<TransactionChartModel> = emptyList()
)

@HiltViewModel
class StatisticsViewModel
@Inject
constructor(
    @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val transactionInteractor: TransactionInteractor,
    currencyInteractor: CurrencyInteractor,
    billingInteractor: BillingInteractor,
) : BaseViewModel<StatisticsNavigator>() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    private val transactions =
        MutableStateFlow<ResultOf<List<TransactionChartModel>>>(ResultOf.Loading())
    private val transactionsInCurrentPeriod =
        MutableStateFlow<ResultOf<Map<LocalDateTime, TransactionLineChartModel>>>(ResultOf.Loading())

    private var currencyCode = DEFAULT_CURRENCY_CODE
    private var categoryType: StatisticsMenuCategoryType = StatisticsMenuCategoryType.Child
    var chartPeriod: StatisticsPeriodModel = StatisticsPeriodModel.DAY
        private set
    var inStatistics: StatisticsMenuInStatistics = StatisticsMenuInStatistics.InStatistics
        private set
    var currencyType: StatisticsMenuCurrency = StatisticsMenuCurrency.Current(currencyCode)
        private set
    var transactionType: StatisticsMenuTransactionType = StatisticsMenuTransactionType.Expense
        private set

    val isPremium =
        billingInteractor
            .isPremium()
            .flowOn(ioDispatcher)
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch {
            currencyCode = currencyInteractor.getMainCurrency().code
            currencyType = StatisticsMenuCurrency.Current(currencyCode)
            _uiState.value.statisticsMenuState.menuMap[StatisticsMenuType.CURRENCY] =
                StatisticsMenuCurrency.Current(currencyCode)
        }
    }
}
