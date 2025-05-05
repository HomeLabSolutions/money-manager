package com.d9tilov.android.statistics.ui.model

import androidx.annotation.DrawableRes
import com.d9tilov.android.statistics.data.model.BaseStatisticsMenuType
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.R

sealed class StatisticsMenuInStatisticsType(
    @DrawableRes val iconId: Int,
) : BaseStatisticsMenuType {
    data object InStatisticsType : StatisticsMenuInStatisticsType(R.drawable.ic_in_statistics)

    data object All : StatisticsMenuInStatisticsType(com.d9tilov.android.designsystem.R.drawable.ic_not_in_statistics)

    override val menuType: StatisticsMenuType = StatisticsMenuType.STATISTICS
}
