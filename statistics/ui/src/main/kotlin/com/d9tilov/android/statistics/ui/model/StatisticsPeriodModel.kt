package com.d9tilov.android.statistics.ui.model

import androidx.annotation.StringRes
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.statistics.ui.R
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

sealed class StatisticsPeriodModel(
    @field:StringRes val name: Int,
    val from: LocalDateTime,
    val to: LocalDateTime,
) {
    data object DAY : StatisticsPeriodModel(
        R.string.statistics_period_day,
        currentDate().getStartOfDay(),
        currentDate().getEndOfDay(),
    )

    data object WEEK : StatisticsPeriodModel(
        R.string.statistics_period_week,
        currentDate().minus(1, DateTimeUnit.WEEK).getStartOfDay(),
        currentDate().getEndOfDay(),
    )

    data object MONTH : StatisticsPeriodModel(
        R.string.statistics_period_month,
        currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        currentDate().getEndOfDay(),
    )

    data object YEAR : StatisticsPeriodModel(
        R.string.statistics_period_year,
        currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay(),
        currentDate().getEndOfDay(),
    )

    data class CUSTOM(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_custom,
            fromDate.getStartOfDay(),
            toDate.getEndOfDay(),
        )
}
