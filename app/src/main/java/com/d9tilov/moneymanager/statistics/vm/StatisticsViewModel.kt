package com.d9tilov.moneymanager.statistics.vm

import com.d9tilov.moneymanager.base.ui.navigator.StatisticsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsChartMode
import com.d9tilov.moneymanager.transaction.domain.entity.StatisticsPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : BaseViewModel<StatisticsNavigator>() {

    var chartMode: StatisticsChartMode = StatisticsChartMode.PIE_CHART
    var chartPeriod: StatisticsPeriod = StatisticsPeriod.MONTH
}
