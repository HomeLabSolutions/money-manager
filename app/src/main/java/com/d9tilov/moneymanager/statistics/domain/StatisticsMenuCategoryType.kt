package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuCategoryType(val name: String) : BaseStatisticsMenuType {

    companion object {
        private const val STATISTICS_PARENT_NAME = "statistics_parent_name"
        private const val STATISTICS_CHILD_NAME = "statistics_child_name"
    }

    @Parcelize
    object PARENT : StatisticsMenuCategoryType(STATISTICS_PARENT_NAME)

    @Parcelize
    object CHILD : StatisticsMenuCategoryType(STATISTICS_CHILD_NAME)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CATEGORY_TYPE
}
