package com.d9tilov.moneymanager.statistics.domain

sealed class StatisticsMenuChartMode : BaseStatisticsMenuType {

    object PieChart : StatisticsMenuChartMode()

    object LineChart : StatisticsMenuChartMode()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CHART
}
