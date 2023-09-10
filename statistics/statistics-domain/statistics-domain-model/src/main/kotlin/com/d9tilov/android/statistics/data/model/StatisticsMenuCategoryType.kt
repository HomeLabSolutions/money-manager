package com.d9tilov.android.statistics.data.model

sealed class StatisticsMenuCategoryType : BaseStatisticsMenuType {

    object Parent : StatisticsMenuCategoryType()

    object Child : StatisticsMenuCategoryType()

    override val menuType: StatisticsMenuType = StatisticsMenuType.CATEGORY_TYPE
}
