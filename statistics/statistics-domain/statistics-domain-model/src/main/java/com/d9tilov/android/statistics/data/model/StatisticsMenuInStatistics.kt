package com.d9tilov.android.statistics.data.model

sealed class StatisticsMenuInStatistics : BaseStatisticsMenuType {

    object InStatistics : StatisticsMenuInStatistics()

    object All : StatisticsMenuInStatistics()

    override val menuType: StatisticsMenuType = StatisticsMenuType.STATISTICS
}
