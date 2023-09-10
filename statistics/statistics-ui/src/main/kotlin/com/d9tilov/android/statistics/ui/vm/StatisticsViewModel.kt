package com.d9tilov.android.statistics.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuCategoryType
import com.d9tilov.android.statistics.data.model.StatisticsMenuChartMode
import com.d9tilov.android.statistics.data.model.StatisticsMenuCurrency
import com.d9tilov.android.statistics.data.model.StatisticsMenuInStatistics
import com.d9tilov.android.statistics.data.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsPeriod
import com.d9tilov.android.statistics.data.model.toType
import com.d9tilov.android.statistics.ui.navigation.StatisticsNavigator
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionLineChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    currencyInteractor: CurrencyInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<StatisticsNavigator>() {

    private val transactions =
        MutableStateFlow<ResultOf<List<TransactionChartModel>>>(ResultOf.Loading())
    private val transactionsInCurrentPeriod =
        MutableStateFlow<ResultOf<Map<LocalDateTime, TransactionLineChartModel>>>(ResultOf.Loading())

    private var currencyCode = DEFAULT_CURRENCY_CODE
    private var categoryType: StatisticsMenuCategoryType = StatisticsMenuCategoryType.Child
    var chartMode: StatisticsMenuChartMode = StatisticsMenuChartMode.PieChart
        private set
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.DAY
        private set
    var inStatistics: StatisticsMenuInStatistics = StatisticsMenuInStatistics.InStatistics
        private set
    var currencyType: StatisticsMenuCurrency = StatisticsMenuCurrency.Current(currencyCode)
        private set
    var transactionType: StatisticsMenuTransactionType = StatisticsMenuTransactionType.Expense
        private set

    val menuItemList = MutableList(StatisticsMenuType.values().size) { index ->
        when (toType(index)) {
            StatisticsMenuType.CURRENCY -> currencyType
            StatisticsMenuType.STATISTICS -> inStatistics
            StatisticsMenuType.CHART -> chartMode
            StatisticsMenuType.CATEGORY_TYPE -> categoryType
            StatisticsMenuType.TRANSACTION_TYPE -> transactionType
        }
    }

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch {
            currencyCode = currencyInteractor.getMainCurrency().code
            currencyType = StatisticsMenuCurrency.Current(currencyCode)
            menuItemList[currencyType.menuType.ordinal] = currencyType
            menuItemList[chartMode.menuType.ordinal] = chartMode
            menuItemList[categoryType.menuType.ordinal] = categoryType
            menuItemList[transactionType.menuType.ordinal] = transactionType
            menuItemList[inStatistics.menuType.ordinal] = inStatistics
        }
    }

    fun updateCurrency() {
        currencyType = when (currencyType) {
            StatisticsMenuCurrency.Default -> StatisticsMenuCurrency.Current(this.currencyCode)
            is StatisticsMenuCurrency.Current -> StatisticsMenuCurrency.Default
        }
        updateItemInList(currencyType)
        update()
    }

    fun updatePeriod(period: StatisticsPeriod) {
        chartPeriod = period
        update()
    }

    fun updateStatisticsFlag() {
        inStatistics = when (inStatistics) {
            StatisticsMenuInStatistics.InStatistics -> StatisticsMenuInStatistics.All
            StatisticsMenuInStatistics.All -> StatisticsMenuInStatistics.InStatistics
        }
        updateItemInList(inStatistics)
        update()
    }

    fun updateTransactionType() {
        transactionType = when (transactionType) {
            StatisticsMenuTransactionType.Income -> StatisticsMenuTransactionType.Expense
            StatisticsMenuTransactionType.Expense -> StatisticsMenuTransactionType.Income
        }
        updateItemInList(transactionType)
        update()
    }

    fun updateCharMode() {
        chartMode = when (chartMode) {
            StatisticsMenuChartMode.LineChart -> StatisticsMenuChartMode.PieChart
            StatisticsMenuChartMode.PieChart -> StatisticsMenuChartMode.LineChart
        }
        updateItemInList(chartMode)
        update()
    }

    fun updateCategoryType() {
        categoryType = when (categoryType) {
            StatisticsMenuCategoryType.Child -> StatisticsMenuCategoryType.Parent
            StatisticsMenuCategoryType.Parent -> StatisticsMenuCategoryType.Child
        }
        updateItemInList(categoryType)
        update()
    }

    private fun update() {
        updateTransactions()
        if (chartMode == StatisticsMenuChartMode.LineChart) {
            updatePeriodTransactions()
        }
    }

    private fun updateTransactions() {
        viewModelScope.launch {
            transactionInteractor.getTransactionsGroupedByCategory(
                when (transactionType) {
                    StatisticsMenuTransactionType.Expense -> TransactionType.EXPENSE
                    StatisticsMenuTransactionType.Income -> TransactionType.INCOME
                },
                chartPeriod.from,
                chartPeriod.to,
                currencyType.currencyCode,
                inStatistics == StatisticsMenuInStatistics.InStatistics,
                categoryType == StatisticsMenuCategoryType.Child
            )
                .map { list -> list.sortedByDescending { tr -> tr.sum } }
                .catch { transactionsInCurrentPeriod.value = ResultOf.Failure(it) }
                .collect { transactions.value = ResultOf.Success(it) }
        }
    }

    private fun updatePeriodTransactions() {
        viewModelScope.launch {
            transactionInteractor.getTransactionsGroupedByDate(
                when (transactionType) {
                    StatisticsMenuTransactionType.Expense -> TransactionType.EXPENSE
                    StatisticsMenuTransactionType.Income -> TransactionType.INCOME
                },
                chartPeriod.from,
                chartPeriod.to,
                currencyType.currencyCode,
                inStatistics == StatisticsMenuInStatistics.InStatistics
            )
                .catch { transactionsInCurrentPeriod.value = ResultOf.Failure(it) }
                .collect { transactionsInCurrentPeriod.value = ResultOf.Success(it) }
        }
    }

    private fun updateItemInList(item: BaseStatisticsMenuType) {
        val index = item.menuType.ordinal
        menuItemList[index] = item
    }

    fun getTransactions(): StateFlow<ResultOf<List<TransactionChartModel>>> = transactions
    fun getTransactionsInCurrentPeriod(): StateFlow<ResultOf<Map<LocalDateTime, TransactionLineChartModel>>> =
        transactionsInCurrentPeriod
}
