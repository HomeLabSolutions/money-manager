package com.d9tilov.moneymanager.statistics.vm

import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val transactionInteractor: TransactionInteractor) :
    BaseViewModel<StatisticsNavigator>() {

    val currency: LiveData<String> =
        flow { emit(transactionInteractor.getCurrentCurrencyCode()) }.flowOn(Dispatchers.IO)
            .asLiveData()
    private val transactionsLiveData = MutableLiveData<List<TransactionChartModel>>()

    var chartMode: StatisticsMenuChartMode = StatisticsMenuChartMode.PIE_CHART
        private set
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.DAY
        private set
    var inStatistics: StatisticsMenuInStatistics = StatisticsMenuInStatistics.IN_STATISTICS
        private set
    var currencyType: StatisticsMenuCurrency = StatisticsMenuCurrency.CURRENT(DEFAULT_CURRENCY_CODE)
        private set
    var transactionType: StatisticsMenuTransactionType = StatisticsMenuTransactionType.EXPENSE
        private set
    var categoryType: StatisticsMenuCategoryType = StatisticsMenuCategoryType.CHILD
        private set

    var currencyCode: String = DEFAULT_CURRENCY_CODE
    var from: LocalDateTime = currentDateTime().getStartOfDay()
        private set
    var to: LocalDateTime = currentDateTime().getEndOfDay()
        private set

    val menuItemList = mutableListOf(
        chartMode,
        categoryType,
        transactionType,
        inStatistics
    )

    fun updateCurrency(currencyCode: String? = null) {
        if (currencyCode == null) {
            currencyType = when (currencyType) {
                StatisticsMenuCurrency.DEFAULT -> StatisticsMenuCurrency.CURRENT(this.currencyCode)
                is StatisticsMenuCurrency.CURRENT -> StatisticsMenuCurrency.DEFAULT
            }
        } else {
            this.currencyCode = currencyCode
            currencyType = StatisticsMenuCurrency.CURRENT(currencyCode)
            if (!menuItemList.contains(currencyType)) {
                menuItemList.add(0, currencyType)
            }
        }
        updateItemInList(currencyType, 0)
        update()
    }

    fun updatePeriod(
        period: StatisticsPeriod,
        periodPair: Pair<LocalDateTime, LocalDateTime>? = null
    ) {
        chartPeriod = period
        if (periodPair == null) {
            val toCurrentDate = currentDate()
            val fromCurrentDate = when (chartPeriod) {
                StatisticsPeriod.DAY -> toCurrentDate
                StatisticsPeriod.WEEK -> toCurrentDate.minus(1, DateTimeUnit.WEEK)
                StatisticsPeriod.MONTH -> toCurrentDate.minus(1, DateTimeUnit.MONTH)
                StatisticsPeriod.YEAR -> toCurrentDate.minus(1, DateTimeUnit.YEAR)
                StatisticsPeriod.CUSTOM -> throw IllegalArgumentException("Wrong period type with nullable period pair")
            }
            this.from = fromCurrentDate.getStartOfDay()
            this.to = toCurrentDate.getEndOfDay()
        } else {
            this.from = periodPair.first?.getStartOfDay() ?: currentDateTime().getStartOfDay()
            this.to = periodPair.second?.getEndOfDay() ?: currentDateTime().getEndOfDay()
        }
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
                from.getStartOfDay(),
                to.getEndOfDay(),
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
        menuItemList.removeAt(index)
        menuItemList.add(index, item)
    }

    fun getTransactions(): LiveData<List<TransactionChartModel>> = transactionsLiveData
}
