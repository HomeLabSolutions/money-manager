package com.d9tilov.moneymanager.statistics.domain

sealed class StatisticsMenuCategoryType : BaseStatisticsMenuType {

    object Parent : StatisticsMenuCategoryType()

    object Child : StatisticsMenuCategoryType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CATEGORY_TYPE
}
