package com.d9tilov.moneymanager.statistics.domain

import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

sealed class StatisticsPeriod(val name: String, val from: LocalDateTime, val to: LocalDateTime) {

    companion object {
        private const val STATISTICS_PERIOD_DAY_NAME = "statistics_period_day"
        private const val STATISTICS_PERIOD_WEEK_NAME = "statistics_period_week"
        private const val STATISTICS_PERIOD_MONTH_NAME = "statistics_period_month"
        private const val STATISTICS_PERIOD_YEAR_NAME = "statistics_period_year"
        private const val STATISTICS_PERIOD_CUSTOM_NAME = "statistics_period_custom"
    }

    object DAY : StatisticsPeriod(
        STATISTICS_PERIOD_DAY_NAME,
        currentDate().getStartOfDay(),
        currentDate().getEndOfDay()
    )

    object WEEK : StatisticsPeriod(
        STATISTICS_PERIOD_WEEK_NAME,
        currentDate().minus(1, DateTimeUnit.WEEK).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    object MONTH : StatisticsPeriod(
        STATISTICS_PERIOD_MONTH_NAME,
        currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    object YEAR : StatisticsPeriod(
        STATISTICS_PERIOD_YEAR_NAME,
        currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay(),
        currentDate().getEndOfDay()
    )

    data class CUSTOM(val fromDate: LocalDateTime, val toDate: LocalDateTime) :
        StatisticsPeriod(
            STATISTICS_PERIOD_CUSTOM_NAME,
            fromDate.getStartOfDay(),
            toDate.getEndOfDay()
        )
}
