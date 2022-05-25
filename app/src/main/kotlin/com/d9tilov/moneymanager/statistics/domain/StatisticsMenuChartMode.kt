package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuChartMode : BaseStatisticsMenuType {

    @Parcelize
    object PieChart : StatisticsMenuChartMode()

    @Parcelize
    object LineChart : StatisticsMenuChartMode()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CHART
}
