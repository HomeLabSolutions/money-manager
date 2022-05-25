package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuInStatistics : BaseStatisticsMenuType {

    @Parcelize
    object InStatistics : StatisticsMenuInStatistics()

    @Parcelize
    object All : StatisticsMenuInStatistics()

    override val menuType: StatisticsMenuType = StatisticsMenuType.STATISTICS
}
