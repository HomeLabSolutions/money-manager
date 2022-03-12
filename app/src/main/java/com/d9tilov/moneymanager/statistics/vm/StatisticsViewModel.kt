package com.d9tilov.moneymanager.statistics.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsChartMode
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val transactionInteractor: TransactionInteractor) :
    BaseViewModel<StatisticsNavigator>() {

    var chartMode: StatisticsChartMode = StatisticsChartMode.PIE_CHART
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.DAY
        private set

    private val transactionsLiveData = MutableLiveData<List<TransactionChartModel>>()

    fun updatePeriod(period: StatisticsPeriod) = viewModelScope.launch(Dispatchers.Main) {
        chartPeriod = period
        val to = currentDate()
        val from = when (chartPeriod) {
            StatisticsPeriod.DAY -> to
            StatisticsPeriod.WEEK -> to.minus(1, DateTimeUnit.WEEK)
            StatisticsPeriod.MONTH -> to.minus(1, DateTimeUnit.MONTH)
            StatisticsPeriod.YEAR -> to.minus(1, DateTimeUnit.YEAR)
            StatisticsPeriod.CUSTOM -> to.minus(1, DateTimeUnit.YEAR)
        }
        transactionInteractor.getTransactionsGroupedByCategory(
            TransactionType.EXPENSE,
            from.getStartOfDay(),
            to.getEndOfDay(),
            true
        )
            .flowOn(Dispatchers.IO)
            .collect { transactionsLiveData.value = it }
    }

    fun getTransactions(): LiveData<List<TransactionChartModel>> = transactionsLiveData
}
