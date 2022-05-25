package com.d9tilov.moneymanager.statistics.domain

import kotlinx.parcelize.Parcelize

sealed class StatisticsMenuCategoryType : BaseStatisticsMenuType {

    @Parcelize
    object Parent : StatisticsMenuCategoryType()

    @Parcelize
    object Child : StatisticsMenuCategoryType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CATEGORY_TYPE
}
