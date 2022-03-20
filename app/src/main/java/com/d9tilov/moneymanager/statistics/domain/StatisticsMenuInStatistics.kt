package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuInStatistics(val name: String) : BaseStatisticsMenuType {

    companion object {
        private const val STATISTICS_IN_STATISTICS_NAME = "statistics_in_statistics_name"
        private const val STATISTICS_ALL_NAME = "statistics_all_name"
    }

    @Parcelize
    object IN_STATISTICS : StatisticsMenuInStatistics(STATISTICS_IN_STATISTICS_NAME)

    @Parcelize
    object ALL : StatisticsMenuInStatistics(STATISTICS_ALL_NAME)

    override val menuType: StatisticsMenuType = StatisticsMenuType.STATISTICS
}
