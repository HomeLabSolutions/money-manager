package com.d9tilov.android.statistics.ui.model

import androidx.annotation.DrawableRes
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics_ui.R

sealed class StatisticsMenuCategoryType(@DrawableRes val iconId: Int) : BaseStatisticsMenuType {
    data object Parent : StatisticsMenuCategoryType(R.drawable.ic_statistics_root)

    data object Child : StatisticsMenuCategoryType(R.drawable.ic_statistics_child)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CATEGORY_TYPE
}
