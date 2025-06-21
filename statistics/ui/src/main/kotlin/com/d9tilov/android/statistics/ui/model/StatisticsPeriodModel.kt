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
    @StringRes val name: Int,
    val from: LocalDateTime,
    val to: LocalDateTime,
) {
    data class DAY(
        val fromDate: LocalDateTime = currentDate().getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_day,
            fromDate,
            toDate,
        )

    data class WEEK(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.WEEK).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_week,
            fromDate,
            toDate,
        )

    data class MONTH(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_month,
            fromDate,
            toDate,
        )

    data class YEAR(
        val fromDate: LocalDateTime = currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay(),
        val toDate: LocalDateTime = currentDate().getEndOfDay(),
    ) : StatisticsPeriodModel(
            R.string.statistics_period_year,
            fromDate,
            toDate,
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
