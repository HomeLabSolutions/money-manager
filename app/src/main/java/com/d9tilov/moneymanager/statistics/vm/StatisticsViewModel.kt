package com.d9tilov.moneymanager.statistics.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.statistics.domain.BaseStatisticsMenuType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCategoryType
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuChartMode
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuCurrency
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuInStatistics
import com.d9tilov.moneymanager.statistics.domain.StatisticsMenuTransactionType
import com.d9tilov.moneymanager.statistics.domain.StatisticsPeriod
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    currencyInteractor: CurrencyInteractor
) : BaseViewModel<StatisticsNavigator>() {

    private val transactionsLiveData = MutableLiveData<List<TransactionChartModel>>()

    private val currencyCode = currencyInteractor.getCurrentCurrency().code
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
    var categoryType: StatisticsMenuCategoryType = StatisticsMenuCategoryType.CHILD
        private set

    val menuItemList = mutableListOf(
        currencyType,
        chartMode,
        categoryType,
        transactionType,
        inStatistics
    )

    fun updateCurrency() {
        currencyType = when (currencyType) {
            StatisticsMenuCurrency.DEFAULT -> StatisticsMenuCurrency.CURRENT(this.currencyCode)
            is StatisticsMenuCurrency.CURRENT -> StatisticsMenuCurrency.DEFAULT
        }
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

    fun update() {
        viewModelScope.launch(Dispatchers.Main) {
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
                .collect { transactionsLiveData.value = it }
        }
    }

    private fun updateItemInList(item: BaseStatisticsMenuType, index: Int) {
        menuItemList[index] = item
    }

    fun getTransactions(): LiveData<List<TransactionChartModel>> = transactionsLiveData
}
