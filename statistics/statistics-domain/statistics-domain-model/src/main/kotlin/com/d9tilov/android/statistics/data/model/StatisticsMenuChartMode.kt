package com.d9tilov.android.statistics.data.model

sealed class StatisticsMenuChartMode : BaseStatisticsMenuType {
    object PieChart : StatisticsMenuChartMode()

    object LineChart : StatisticsMenuChartMode()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CHART
}
