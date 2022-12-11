package com.d9tilov.moneymanager.statistics.domain

sealed class StatisticsMenuInStatistics : BaseStatisticsMenuType {

    object InStatistics : StatisticsMenuInStatistics()

    object All : StatisticsMenuInStatistics()

    override val menuType: StatisticsMenuType = StatisticsMenuType.STATISTICS
}
