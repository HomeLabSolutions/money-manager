package com.d9tilov.moneymanager.statistics.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.statistics.domain.BaseStatisticsMenuType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCategoryType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCurrency
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuInStatistics
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuTransactionType
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionLineChartModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    userInteractor: UserInteractor
) : BaseViewModel<StatisticsNavigator>() {

    private val transactions =
        MutableStateFlow<ResultOf<List<TransactionChartModel>>>(ResultOf.Loading())
    private val transactionsInCurrentPeriod =
        MutableStateFlow<ResultOf<Map<LocalDateTime, TransactionLineChartModel>>>(ResultOf.Loading())

    private var currencyCode = DEFAULT_CURRENCY_CODE
    private var categoryType: StatisticsMenuCategoryType = StatisticsMenuCategoryType.CHILD
    var chartMode: StatisticsMenuChartMode = StatisticsMenuChartMode.PIE_CHART
        private set
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.DAY
        private set
    var inStatistics: StatisticsMenuInStatistics = StatisticsMenuInStatistics.IN_STATISTICS
        private set
    var currencyType: StatisticsMenuCurrency = StatisticsMenuCurrency.CURRENT(currencyCode)
        private set
    var transactionType: StatisticsMenuTransactionType = StatisticsMenuTransactionType.EXPENSE
        private set

    val menuItemList = mutableListOf<BaseStatisticsMenuType>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            currencyCode = userInteractor.getCurrentCurrency()
            currencyType = StatisticsMenuCurrency.CURRENT(currencyCode)
            menuItemList.add(currencyType)
            menuItemList.add(chartMode)
            menuItemList.add(categoryType)
            menuItemList.add(transactionType)
            menuItemList.add(inStatistics)
        }
    }

    fun updateCurrency() {
        currencyType = when (currencyType) {
            StatisticsMenuCurrency.DEFAULT -> StatisticsMenuCurrency.CURRENT(this.currencyCode)
            is StatisticsMenuCurrency.CURRENT -> StatisticsMenuCurrency.DEFAULT
        }
        updateItemInList(currencyType, 0)
        update()
    }

    fun updatePeriod(period: StatisticsPeriod) {
        chartPeriod = period
        update()
    }

    fun updateStatisticsFlag() {
        inStatistics = when (inStatistics) {
            StatisticsMenuInStatistics.IN_STATISTICS -> StatisticsMenuInStatistics.ALL
            StatisticsMenuInStatistics.ALL -> StatisticsMenuInStatistics.IN_STATISTICS
        }
        updateItemInList(inStatistics, 4)
        update()
    }

    fun updateTransactionType() {
        transactionType = when (transactionType) {
            StatisticsMenuTransactionType.INCOME -> StatisticsMenuTransactionType.EXPENSE
            StatisticsMenuTransactionType.EXPENSE -> StatisticsMenuTransactionType.INCOME
        }
        updateItemInList(transactionType, 3)
        update()
    }

    fun updateCharMode() {
        chartMode = when (chartMode) {
            StatisticsMenuChartMode.LINE_CHART -> StatisticsMenuChartMode.PIE_CHART
            StatisticsMenuChartMode.PIE_CHART -> StatisticsMenuChartMode.LINE_CHART
        }
        updateItemInList(chartMode, 1)
        update()
    }

    fun updateCategoryType() {
        categoryType = when (categoryType) {
            StatisticsMenuCategoryType.CHILD -> StatisticsMenuCategoryType.PARENT
            StatisticsMenuCategoryType.PARENT -> StatisticsMenuCategoryType.CHILD
        }
        updateItemInList(categoryType, 2)
        update()
    }

    private fun update() {
        updateTransactions()
        if (chartMode == StatisticsMenuChartMode.LINE_CHART) {
            updatePeriodTransactions()
        }
    }

    private fun updateTransactions() {
        viewModelScope.launch {
            transactionInteractor.getTransactionsGroupedByCategory(
                when (transactionType) {
                    StatisticsMenuTransactionType.EXPENSE -> TransactionType.EXPENSE
                    StatisticsMenuTransactionType.INCOME -> TransactionType.INCOME
                },
                chartPeriod.from,
                chartPeriod.to,
                currencyType.currencyCode,
                inStatistics == StatisticsMenuInStatistics.IN_STATISTICS,
                categoryType == StatisticsMenuCategoryType.CHILD
            )
                .map { list -> list.sortedByDescending { tr -> tr.sum } }
                .flowOn(Dispatchers.IO)
                .catch { transactionsInCurrentPeriod.value = ResultOf.Failure(it) }
                .collect { transactions.value = ResultOf.Success(it) }
        }
    }

    private fun updatePeriodTransactions() {
        viewModelScope.launch {
            transactionInteractor.getTransactionsGroupedByDate(
                when (transactionType) {
                    StatisticsMenuTransactionType.EXPENSE -> TransactionType.EXPENSE
                    StatisticsMenuTransactionType.INCOME -> TransactionType.INCOME
                },
                chartPeriod.from,
                chartPeriod.to,
                currencyType.currencyCode,
                inStatistics == StatisticsMenuInStatistics.IN_STATISTICS
            )
                .flowOn(Dispatchers.IO)
                .catch { transactionsInCurrentPeriod.value = ResultOf.Failure(it) }
                .collect { transactionsInCurrentPeriod.value = ResultOf.Success(it) }
        }
    }

    private fun updateItemInList(item: BaseStatisticsMenuType, index: Int) {
        menuItemList[index] = item
    }

    fun getTransactions(): StateFlow<ResultOf<List<TransactionChartModel>>> = transactions
    fun getTransactionsInCurrentPeriod(): StateFlow<ResultOf<Map<LocalDateTime, TransactionLineChartModel>>> =
        transactionsInCurrentPeriod
}
