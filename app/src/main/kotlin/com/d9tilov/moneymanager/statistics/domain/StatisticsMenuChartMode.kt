package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuChartMode(val name: String) : BaseStatisticsMenuType {

    companion object {
        private const val STATISTICS_PIE_CHART_NAME = "statistics_pie_chart_name"
        private const val STATISTICS_LINE_CHART_NAME = "statistics_line_chart_name"
    }

    @Parcelize
    object PIE_CHART : StatisticsMenuChartMode(STATISTICS_PIE_CHART_NAME)

    @Parcelize
    object LINE_CHART : StatisticsMenuChartMode(STATISTICS_LINE_CHART_NAME)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CHART
}
