package com.d9tilov.android.statistics.ui.model

import androidx.annotation.DrawableRes
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics_ui.R

sealed class StatisticsMenuChartModel(
    @DrawableRes open val iconId: Int,
) : BaseStatisticsMenuType {
    data object PieChart : StatisticsMenuChartModel(R.drawable.ic_pie_chart)

    data object LineChart : StatisticsMenuChartModel(R.drawable.ic_line_chart)

    override val menuType: StatisticsMenuType = StatisticsMenuType.CHART
}
