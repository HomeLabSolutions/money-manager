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
import com.d9tilov.moneymanager.statistics.domain.StatisticsChartMode
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

    var chartMode: StatisticsChartMode = StatisticsChartMode.PIE_CHART
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.DAY
        private set
    val currency: LiveData<String> =
        flow { emit(transactionInteractor.getCurrentCurrencyCode()) }.flowOn(Dispatchers.IO)
            .asLiveData()
    var inStatistics: Boolean = true
        private set
    private val transactionsLiveData = MutableLiveData<List<TransactionChartModel>>()
    var from: LocalDateTime = currentDateTime().getStartOfDay()
        private set
    var to: LocalDateTime = currentDateTime().getEndOfDay()
        private set
    var currencyCode: String = DEFAULT_CURRENCY_CODE
        private set

    fun updateCurrency(currencyCode: String? = null) {
        if (currencyCode == null) {
            this.currencyCode = if (this.currencyCode == DEFAULT_CURRENCY_CODE) currency.value
                ?: DEFAULT_CURRENCY_CODE else DEFAULT_CURRENCY_CODE
        } else {
            this.currencyCode = currencyCode
        }
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
        inStatistics = !inStatistics
        update()
    }

    fun update() {
        viewModelScope.launch(Dispatchers.Main) {
            transactionInteractor.getTransactionsGroupedByCategory(
                TransactionType.EXPENSE,
                from.getStartOfDay(),
                to.getEndOfDay(),
                currencyCode,
                inStatistics
            )
                .map { list -> list.sortedByDescending { tr -> tr.sum } }
                .flowOn(Dispatchers.IO)
                .collect { transactionsLiveData.value = it }
        }
    }

    fun getTransactions(): LiveData<List<TransactionChartModel>> = transactionsLiveData
}
